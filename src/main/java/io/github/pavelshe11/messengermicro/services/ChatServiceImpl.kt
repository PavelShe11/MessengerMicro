package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.client.grpc.ParticipantGrpcService
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.response.ChatCreationResponseDto
import io.github.pavelshe11.messengermicro.api.exceptions.ChatNotFoundException
import io.github.pavelshe11.messengermicro.api.exceptions.ServerAnswerException
import io.github.pavelshe11.messengermicro.normalizers.DataNormalizer
import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantTypeEntity
import io.github.pavelshe11.messengermicro.store.repositories.ChatRoomRepository
import io.github.pavelshe11.messengermicro.store.repositories.ChatSendersRepository
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantRepository
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantTypeRepository
import io.github.pavelshe11.messengermicro.validators.ChatDataValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChatServiceImpl(
    private val chatRoomRepository: ChatRoomRepository,
    private val participantRepository: ParticipantRepository,
    private val participantTypeRepository: ParticipantTypeRepository,
    private val chatSendersRepository: ChatSendersRepository,
    private val participantGrpcService: ParticipantGrpcService,
    private val dataNormalizer: DataNormalizer,
    private val dataValidator: ChatDataValidator,
    private val chatDataValidator: ChatDataValidator
) : ChatService {

    @Transactional
    override fun createChat(request: ChatCreationRequestDto, accountId: UUID): ChatCreationResponseDto {
        val normalizedRequest = dataNormalizer.normalizeChatCreationRequest(request)
        dataValidator.validateChatCreationRequest(normalizedRequest)

        val allParticipants = mutableSetOf<ParticipantEntity>()
        val nonExistentParticipants = mutableSetOf<UUID>()

        val ownerParticipantType = getOrCreateParticipantType("account")
        val ownerParticipant = findOrCreateParticipant(accountId, ownerParticipantType)

        if (ownerParticipant == null) {
            log.error("Создатель чата не может быть null")
            throw ServerAnswerException()
        }

        allParticipants.add(ownerParticipant)

        for (participantDto in normalizedRequest.participants) {
            val typeName = participantDto.participantType.ifBlank { "account" }
            val participantType = getOrCreateParticipantType(typeName)

            val participant = findOrCreateParticipant(participantDto.refId, participantType)
            if (participant == null) {
                nonExistentParticipants.add(participantDto.refId)
                continue
            }

            allParticipants.add(participant)
        }

        if (nonExistentParticipants.isNotEmpty()) {
            log.error("Не найдены участники в networking: {}", nonExistentParticipants)
            throw ServerAnswerException()
        }

        val participantIds = allParticipants.mapNotNull { it.id }.toSet()
        val existingChat = findExistingChatWithParticipantsIds(participantIds)
        if (existingChat != null) {
            log.info("Чат с участниками {} уже существует.", participantIds)
            return ChatCreationResponseDto(chatId = existingChat.id!!)
        }

        val newChat = createNewChat(allParticipants)
        log.info("Создан новый чат с участниками: {}", participantIds)
        return ChatCreationResponseDto(
            chatId = newChat.id!!
        )
    }

    private fun createNewChat(participants: Set<ParticipantEntity>): ChatRoomEntity {
        val chatRoom = chatRoomRepository.save(ChatRoomEntity())

        for (participant in participants) {
            val chatSender = ChatSendersEntity(chatRoom = chatRoom, participant = participant)
            chatSendersRepository.save(chatSender)
            log.info("Добавлен участник {} в чат {}", participant.refId, chatRoom.id)
        }
        return chatRoom
    }

    private fun findExistingChatWithParticipantsIds(participantIds: Set<UUID>): ChatRoomEntity? {
        log.info("Вызван метод поиска существующего чата")
        val existingChatIdOptional = chatSendersRepository.findExistingChat(participantIds, participantIds.size)
        if (existingChatIdOptional.isEmpty) return null

        val chatId = existingChatIdOptional.get()
        return chatRoomRepository.findById(chatId).orElse(null)
    }

    private fun getOrCreateParticipantType(typeName: String): ParticipantTypeEntity {
        return participantTypeRepository.findByTypeName(typeName)
            ?: participantTypeRepository.save(ParticipantTypeEntity(typeName = typeName))
    }

    private fun findOrCreateParticipant(
        refId: UUID,
        participantType: ParticipantTypeEntity
    ): ParticipantEntity? {
        val existingParticipant =
            participantRepository.findByRefId(refId)

        if (existingParticipant != null) {
            return existingParticipant
        }

        log.info("До запроса в networking. Участник {}", refId)
        val existsRemoteParticipant = participantGrpcService.existsByRefId(refId)
        log.info("Ответ запроса networking {}", existsRemoteParticipant)

        return if (existsRemoteParticipant) {
            val newParticipant = ParticipantEntity(
                refId = refId,
                participantType = participantType
            )
            participantRepository.save(newParticipant)
        } else {
            null
        }
    }

    @Transactional
    override fun deleteChats(request: ChatDeletingRequestDto) {
        log.info("Вызван метод даления чатов")
        val chatIds = request.chatsIdsToDeleting
        if (chatIds.isNullOrEmpty()) {
            log.error("Список не передан")
            throw ServerAnswerException()
        }

        chatDataValidator.validateChatDeletingRequest(request)

        val nonExistentChats = mutableSetOf<UUID>()

        for (chatIdToDeleting in chatIds) {
            val chatRoom = chatRoomRepository.findById(chatIdToDeleting).orElse(null)
            if (chatRoom == null) {
                nonExistentChats.add(chatIdToDeleting)
                continue
            }

            val sendersInChat = chatSendersRepository.findAllByChatRoom((chatRoom))
            chatSendersRepository.deleteAll(sendersInChat)

            chatRoomRepository.delete(chatRoom)
            log.info("Удаление чата {}", chatRoom.id)

            for (sender in sendersInChat) {
                val participant = sender.participant

                val isParticipantInOtherChats = chatSendersRepository.existsByParticipant(participant)
                if (!isParticipantInOtherChats) {
                    val participantType = participant.participantType

                    participantRepository.delete(participant)

                    val isThisParticipantTypeUsingByAnyone =
                        participantRepository.existsByParticipantType(participantType)

                    if (!isThisParticipantTypeUsingByAnyone) {
                        participantTypeRepository.delete(participantType)
                    }
                }
            }
        }
        if (nonExistentChats.isNotEmpty()) {
            log.error("Не найдены чаты id: {}", nonExistentChats.joinToString(", "))
            throw ChatNotFoundException(nonExistentChats)
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)
    }
}
