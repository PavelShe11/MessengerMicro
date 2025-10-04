package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
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

        validators.add { validateChatSenderId(request.chatSenderId) }

        validators.add { validateMessageText(request.messageText) }

        validators.add { validateMessageStatusType(request.messageStatusType) }

        validators.add { validateParentMessageId(request.parentMessageId) }

        log.info("Валидация запроса отправки сообщения завершена")
        commonValidators.validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    private fun validateParentMessageId(parentMessageId: UUID?): FieldErrorDto? {
        val fieldName = "parentMessageId"

        val message = commonValidators.validateUUID(
            parentMessageId.toString(),
            fieldName
        )
        message?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = parentMessageId))
        }
        return null
    }

    private fun validateMessageStatusType(messageStatusType: MessageStatusType?): FieldErrorDto? {
        val fieldName = "messageStatusType"

        if (messageStatusType == null) {
            return FieldErrorDto(fieldName, commonValidators.getMessage("field.empty"))
        }

        val allowedTypes = MessageStatusType.entries.toSet()
        if (messageStatusType !in allowedTypes) {
            return FieldErrorDto(
                fieldName,
                commonValidators.getMessage("error.messageStatus.invalid"),
            )
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

    private fun validateChatSenderId(chatSenderId: UUID?): FieldErrorDto? {
        val fieldName = "chatSenderId"

        val message2 = commonValidators.validateUUID(
            chatSenderId.toString(),
            fieldName
        )
        val message1 = commonValidators.validateNotBlank(
            chatSenderId.toString(),
            fieldName
        )
        message1?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = chatSenderId))
        }
        message2?.let {
            return (FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = chatSenderId))
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

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MessageDataValidator::class.java)
    }
}