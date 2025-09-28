package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.exceptions.FieldValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

@Component
class ChatDataValidator(
    private val messageSource: MessageSource,
    private val commonValidators: CommonValidators
) {
    fun validateInputAndReturnOrThrow(vararg validators: () -> FieldErrorDto?) {
        val errors = validators.mapNotNull { it() }

        if (errors.isNotEmpty()) {
            throw FieldValidationException(errors.toSet())
        }
    }

    fun validateChatCreationRequest(chatCreationRequestDto: ChatCreationRequestDto) {
        log.info("Валидация запроса создания чата")
        val validators = mutableListOf<() -> FieldErrorDto?>()

        chatCreationRequestDto.participants.forEachIndexed { index, participant ->
            validators.add {
                commonValidators.validateNotBlank(
                    participant.participantType,
                    "participants[$index].participantType"
                )
            }

            validators.add {
                commonValidators.validateUUID(participant.refId.toString(),
                    "participants[$index].refId")
            }

            validators.add {
                val isValidParticipantType = participant.participantType.matches(Regex("^[a-zA-Z_]+$"))
                if (!isValidParticipantType) {
                    return@add FieldErrorDto("participants[$index].participantType",
                        "Тип участника должен содержать только английские буквы и подчеркивания.")
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

        chatDeletingRequestDto.chatsIdsToDeleting?.forEachIndexed { index, chatId->
            validators.add {
                commonValidators.validateUUID(chatId.toString(),
                    "chatsIdsToDeleting[$index]")
            }
        }
        log.info("Валидация запроса удаления чата завершена")
        validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

//    protected fun createFieldErrorDto(
//        field: String?,
//        obj: Array<Any?>?,
//        message: String?
//    ): FieldErrorDto {
//        return FieldErrorDto(
//            field,
//            messageSource.getMessage(message, obj, LocaleContextHolder.getLocale())
//        )
//    }
//
//    protected fun createFieldErrorDto(
//        field: String?, obj: Array<Any?>?, message: String?,
//        objectId: UUID?
//    ): FieldErrorDto {
//        return FieldErrorDto(
//            field,
//            messageSource.getMessage(message, obj, LocaleContextHolder.getLocale()),
//            objectId
//        )
//    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatDataValidator::class.java)
    }
}