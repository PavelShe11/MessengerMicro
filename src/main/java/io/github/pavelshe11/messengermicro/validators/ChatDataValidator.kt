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

        chatCreationRequestDto.participants.forEachIndexed { index, participant ->

            validators.add {
                val message = commonValidators.validateNotBlank(
                    participant.participantType,
                    "participants[$index].participantType"
                )
                message?.let {
                    return@add FieldErrorDto(
                        "participants[$index].participantType",
                        getMessage(it)
                    )
                }
                null
            }

            validators.add {
                val message = commonValidators.validateUUID(
                    participant.refId.toString(),
                    "participants[$index].refId"
                )
                message?.let {
                    return@add FieldErrorDto(
                        "participants[$index].refId",
                        getMessage(it)
                    )
                }
            }

            validators.add {
                val isValid = participant.participantType.matches(Regex("^[a-zA-Z_]+$"))
                if (!isValid) {
                    return@add FieldErrorDto(
                        "participants[$index].participantType",
                        getMessage("error.participantType.invalid")
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

        chatDeletingRequestDto.chatsIdsToDeleting?.forEachIndexed { index, chatId ->
            validators.add {
                val message = commonValidators.validateUUID(
                    chatId.toString(),
                    "chatsIdsToDeleting[$index]"
                )
                message?.let {
                    return@add FieldErrorDto("chatsIdsToDeleting[$index]", getMessage(it))
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