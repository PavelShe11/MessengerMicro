package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.client.grpc.ParticipantGrpcService
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantTypeEntity
import io.github.pavelshe11.messengermicro.store.repositories.ChatRoomRepository
import io.github.pavelshe11.messengermicro.store.repositories.ChatSendersRepository
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantRepository
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantTypeRepository
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
    private val participantGrpcService: ParticipantGrpcService
) : ChatService {
    private val log: Logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)

    @Transactional
    override fun createChat(request: ChatCreationRequestDto) {
        val chatRoom = chatRoomRepository.save(ChatRoomEntity())

        for (participantDto in request.participants) {
            val participantType = participantTypeRepository
                .findByTypeName(participantDto.participantType)
                ?: participantTypeRepository.save(
                    ParticipantTypeEntity(typeName = participantDto.participantType)
                )
            log.info("Добавлен тип участника {}", participantDto.participantType)

            val participant = findOrCreateParticipant(
                refId = participantDto.refId,
                participantType = participantType
            )

            val chatSenders = ChatSendersEntity(
                chatRoom = chatRoom,
                participant = participant
            )

            chatSendersRepository.save(chatSenders)
            log.info("Создан чат {}", chatSenders.chatRoom)
        }
    }

    private fun findOrCreateParticipant(
        refId: UUID,
        participantType: ParticipantTypeEntity
    ): ParticipantEntity {
        val existingParticipant =
            participantRepository.findByRefId(refId)

        if (existingParticipant != null) {
            return existingParticipant
        }

        val remoteParticipant = participantGrpcService.existsByRefId(refId)

        if (remoteParticipant != null) {
            log.error("Сущности с refId {} нет", refId)
            //TODO: бросить ServerAnswerException
        }

        val newParticipant = ParticipantEntity(
            refId = refId,
            participantType = participantType
        )

        return newParticipant
    }

    @Transactional
    override fun deleteChats(request: ChatDeletingRequestDto) {

        val chatIds = request.chatsIdsToDeleting
        if (chatIds.isNullOrEmpty()) {
            log.error("Список не передан")
            TODO("выбрасывать исключение пустго списка")
        }

        for (chatIdToDeleting in chatIds) {
            val chatRoom = chatRoomRepository.findById(chatIdToDeleting).orElse(null) ?: continue

            val sendersInChat = chatSendersRepository.findAllByChatRoom((chatRoom))
            chatSendersRepository.deleteAll(sendersInChat)

            chatRoomRepository.delete(chatRoom)

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
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)
    }
}
