package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.client.grpc.ParticipantGrpcService
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
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
    override fun createChat(request: ChatCreationRequestDto, accountId: UUID) {
        val normalizedRequest = dataNormalizer.normalizeChatCreationRequest(request)
        dataValidator.validateChatCreationRequest(normalizedRequest)

        val chatRoom = chatRoomRepository.save(ChatRoomEntity())

        val ownerParticipantType = getOrCreateParticipantType("account")

        val ownerParticipant = findOrCreateParticipant(
            refId = accountId,
            participantType = ownerParticipantType
        )

        val ownerChatSenders = ChatSendersEntity(
            chatRoom = chatRoom,
            participant = requireNotNull(ownerParticipant) {log.error("Создатель чата не может быть null")}
        )

        chatSendersRepository.save(ownerChatSenders)
        log.info("Чат создан с владельцем и первым участником {}", ownerParticipant.refId)
        val nonExistentParticipants = mutableSetOf<UUID>()

        for (participantDto in normalizedRequest.participants) {
            val participantType = getOrCreateParticipantType(participantDto.participantType)
            log.info("Добавлен тип участника {}", participantDto.participantType)

            val participant = findOrCreateParticipant(
                refId = participantDto.refId,
                participantType = participantType
            )

            if (participant == null) {
                nonExistentParticipants.add(participantDto.refId)
                continue
            }

            val chatSenders = ChatSendersEntity(
                chatRoom = chatRoom,
                participant = participant
            )

            chatSendersRepository.save(chatSenders)
            log.info("Добавлен участник {}", participant.refId)
            log.info("Создан чат {}", chatSenders.chatRoom)
        }

        if (nonExistentParticipants.isNotEmpty()) {
            log.error("Не найдены участники с refId: {}", nonExistentParticipants.joinToString(", "))
            throw ServerAnswerException()
        }
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
        val remoteParticipant = participantGrpcService.existsByRefId(refId)
        log.info("Ответ запроса networking {}", remoteParticipant)

        return if (remoteParticipant) {
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
            throw ServerAnswerException()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)
    }
}
