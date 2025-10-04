package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ChatDataValidator(
    private val commonValidators: CommonValidators
) {
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
                        commonValidators.getMessage(it),
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
                        commonValidators.getMessage(it),
                        objectId = participant.refId
                    )
                }
            }

            validators.add {
                val isValid = participant.participantType.matches(Regex("^[a-zA-Z_]+$"))
                if (!isValid) {
                    return@add FieldErrorDto(
                        fieldNameParticipantType,
                        commonValidators.getMessage("error.participantType.invalid"),
                        objectId = participant.refId
                    )
                }
                null
            }
        }
        log.info("Валидация запроса создания чата завершена")
            commonValidators.validateInputAndReturnOrThrow(*validators.toTypedArray())
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
                    return@add FieldErrorDto(fieldName, commonValidators.getMessage(it), objectId = chatId)
                }
                null
            }
        }
        log.info("Валидация запроса удаления чата завершена")
        commonValidators.validateInputAndReturnOrThrow(*validators.toTypedArray())
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatDataValidator::class.java)
    }
}