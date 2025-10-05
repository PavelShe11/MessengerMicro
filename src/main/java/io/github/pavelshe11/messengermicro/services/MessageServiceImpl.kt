package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import io.github.pavelshe11.messengermicro.api.exceptions.MessageNotFoundException
import io.github.pavelshe11.messengermicro.api.exceptions.ServerAnswerException
import io.github.pavelshe11.messengermicro.normalizers.DataNormalizer
import io.github.pavelshe11.messengermicro.store.entities.MessageEntity
import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import io.github.pavelshe11.messengermicro.store.repositories.ChatRoomRepository
import io.github.pavelshe11.messengermicro.store.repositories.ChatSendersRepository
import io.github.pavelshe11.messengermicro.store.repositories.MessageRepository
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantRepository
import io.github.pavelshe11.messengermicro.validators.MessageDataValidator
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class MessageServiceImpl(
    private val dataNormalizer: DataNormalizer,
    private val dataValidator: MessageDataValidator,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatSendersRepository: ChatSendersRepository,
    private val messageRepository: MessageRepository,
    private val participantRepository: ParticipantRepository,
    private val messageStatusService: MessageStatusService,


    ) : MessageService {
    override fun sendMessage(request: MessageSendingRequestDto, accountId: UUID) {
        val normalizedRequest = dataNormalizer.normalizeMessageSendingRequest(request)
        dataValidator.validateMessageSendingRequest(normalizedRequest)

        val chatRoom = chatRoomRepository.findById(normalizedRequest.chatRoomId!!).orElseThrow {
            log.error("Чат с id ${normalizedRequest.chatRoomId} не найден")
            ServerAnswerException()
        }

        val participant = participantRepository.findByRefId(accountId) ?: run {
            log.error("Участник с ref id $accountId не найден")
            throw ServerAnswerException()
        }

        val chatSendersList = chatSendersRepository.findByChatRoomAndParticipant(chatRoom, participant)
        val chatSender = chatSendersList.firstOrNull() ?: run {
            log.error("Участник $accountId не является членом чата ${chatRoom.id}")
            throw ServerAnswerException()
        }

        val parentMessage = normalizedRequest.parentMessageId?.let {
            messageRepository.findById(it).orElseThrow {
                log.error("Сообщение с id ${normalizedRequest.parentMessageId} не найдено")
                ServerAnswerException()
            }
        }

        val message = MessageEntity(
            chatRoom = chatRoom,
            chatSenders = chatSender,
            messageText = normalizedRequest.messageText,
            messageStatusType = MessageStatusType.SENT,
            parentMessage = parentMessage,
            draft = false,
            sendingTime = Instant.now(),
            updatedAt = Instant.now()
        )

        messageRepository.save(message)

        // TODO: отправить в Kafka / WebSocket

    }

    @Transactional
    override fun deleteMessage(request: MessageDeletingRequestDto) {
        log.info("Вызван метод удаления сообщений")
        val messagesIds = request.messagesIdsToDeleting
        if (messagesIds.isNullOrEmpty()) {
            log.error("Список не передан")
            throw ServerAnswerException()
        }

        dataValidator.validateMessagesDeletingRequest(request)

        val nonExistentMessages = mutableSetOf<UUID>()

        for (messageIdToDeleting in messagesIds) {
            val message = messageRepository.findById(messageIdToDeleting).orElse(null)
            if (message == null) {
                nonExistentMessages.add(messageIdToDeleting)
                continue
            }

            messageRepository.delete(message)

            log.info("Удаление сообщения {}", messageIdToDeleting)
        }


        if (nonExistentMessages.isNotEmpty()) {
            log.error("Не найдены сообщения с id: {}", nonExistentMessages.joinToString(", "))
            throw MessageNotFoundException(nonExistentMessages)
        }

        // TODO: отправить в Kafka / WebSocket
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}