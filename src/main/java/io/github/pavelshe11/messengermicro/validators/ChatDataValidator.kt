package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.exceptions.FieldValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChatDataValidator(
    private val messageSource: MessageSource,
    private val commonValidators: CommonValidators
) {
    private fun getMessage(code: String, args: Array<Any>? = null): String =
        messageSource.getMessage(code, args, Locale.getDefault())

    fun validateInputAndReturnOrThrow(vararg validators: () -> FieldErrorDto?) {
        val errors = validators.mapNotNull { it() }

        if (errors.isNotEmpty()) {
            errors.forEach { log.error("Validation error: field = ${it.field}, message = ${it.message}") }

            throw FieldValidationException(errors.toSet())
        }
    }

    fun validateChatCreationRequest(chatCreationRequestDto: ChatCreationRequestDto) {
        log.info("Валидация запроса создания чата")
        val validators = mutableListOf<() -> FieldErrorDto?>()
        val fieldNameParticipantType = "participantType"
        chatCreationRequestDto.participants.forEachIndexed { _, participant ->

            validators.add {
                val message = commonValidators.validateNotBlank(
                    participant.participantType,
                    fieldNameParticipantType
                )
                message?.let {
                    return@add FieldErrorDto(
                        fieldNameParticipantType,
                        getMessage(it),
                        objectId = participant.refId
                    )
                }
                null
            }

            validators.add {
                val fieldName = "refId"
                val message = commonValidators.validateUUID(
                    participant.refId.toString(),
                    fieldName
                )
                message?.let {
                    return@add FieldErrorDto(
                        fieldName,
                        getMessage(it),
                        objectId = participant.refId
                    )
                }
            }

            validators.add {
                val isValid = participant.participantType.matches(Regex("^[a-zA-Z_]+$"))
                if (!isValid) {
                    return@add FieldErrorDto(
                        fieldNameParticipantType,
                        getMessage("error.participantType.invalid"),
                        objectId = participant.refId
                    )
                }
                null
            }
        }
        log.info("Валидация запроса создания чата завершена")
        validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    fun validateChatDeletingRequest(chatDeletingRequestDto: ChatDeletingRequestDto) {
        log.info("Валидация запроса удаления чата")
        val validators = mutableListOf<() -> FieldErrorDto?>()
        val fieldName = "chatsIdsToDeleting"

        chatDeletingRequestDto.chatsIdsToDeleting?.forEachIndexed { _, chatId ->
            validators.add {

                val message = commonValidators.validateUUID(
                    chatId.toString(),
                    fieldName
                )
                message?.let {
                    return@add FieldErrorDto(fieldName, getMessage(it), objectId = chatId)
                }
                null
            }
        }
        log.info("Валидация запроса удаления чата завершена")
        validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatDataValidator::class.java)
    }
}