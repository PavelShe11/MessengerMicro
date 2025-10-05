package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.exceptions.MessageNotFoundException
import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import io.github.pavelshe11.messengermicro.store.repositories.MessageRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class MessageStatusService(
    private val messageRepository: MessageRepository,
) {
    fun markAsRead(messageId: UUID) {
        updateMessageStatus(messageId, MessageStatusType.READ)
    }

    fun markAsDelivered(messageId: UUID) {
        updateMessageStatus(messageId, MessageStatusType.DELIVERED)
    }

    fun markAsError(messageId: UUID) {
        updateMessageStatus(messageId, MessageStatusType.ERROR)
    }

    private fun updateMessageStatus(messageId: UUID, statusType: MessageStatusType) {
        val message = messageRepository.findById(messageId).orElseThrow {
            log.error("Сообщение $messageId не найдено")
            throw MessageNotFoundException(setOf(messageId))
        }
        message.messageStatusType = statusType
        message.updatedAt = Instant.now()
        messageRepository.save(message)
    }

    companion object {
        private val log = LoggerFactory.getLogger(MessageStatusService::class.java)
    }
}