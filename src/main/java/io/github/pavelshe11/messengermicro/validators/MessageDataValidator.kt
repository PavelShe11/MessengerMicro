package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageDataValidator(
    private val commonValidators: CommonValidators,
) {

    fun validateMessageSendingRequest(request: MessageSendingRequestDto) {
        log.info("Валидация запроса отправки сообщения")
        val validators = mutableListOf<() -> FieldErrorDto?>()

        validators.add { validateChatRoomId(request.chatRoomId) }

        validators.add { validateMessageText(request.messageText) }


        validators.add { validateParentMessageId(request.parentMessageId) }

        log.info("Валидация запроса отправки сообщения завершена")
        commonValidators.validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    private fun validateParentMessageId(parentMessageId: UUID?): FieldErrorDto? {
        val fieldName = "parentMessageId"

        if (parentMessageId == null) {
            return null
        }

        val message = commonValidators.validateUUID(
            parentMessageId.toString(),
            fieldName
        )
        message?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = parentMessageId))
        }
        return null
    }

    private fun validateMessageText(messageText: String?): FieldErrorDto? {
        val fieldName = "messageText"

        val messageBlank = commonValidators.validateNotBlank(
            messageText,
            fieldName
        )
        messageBlank?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it)))
        }

        return null
    }

    private fun validateChatRoomId(chatRoomId: UUID?): FieldErrorDto? {
        val fieldName = "chatRoomId"

        val message2 = commonValidators.validateUUID(
            chatRoomId.toString(),
            fieldName
        )
        val message1 = commonValidators.validateNotBlank(
            chatRoomId.toString(),
            fieldName
        )
        message1?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = chatRoomId))
        }
        message2?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = chatRoomId))
        }
        return null
    }

    fun validateMessagesDeletingRequest(request: MessageDeletingRequestDto) {
        log.info("Валидация запроса удаления сообщений")
        val validators = mutableListOf<() -> FieldErrorDto?>()
        val fieldName = "messagesIdsToDeleting"

        request.messagesIdsToDeleting?.forEachIndexed { _, messageId ->
            validators.add {

                val message = commonValidators.validateUUID(
                    messageId.toString(),
                    fieldName
                )
                message?.let {
                    return@add FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = messageId)
                }
                null
            }
        }
        log.info("Валидация запроса удаления сообщений завершена")
        commonValidators.validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MessageDataValidator::class.java)
    }
}