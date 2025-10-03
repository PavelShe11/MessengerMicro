package io.github.pavelshe11.messengermicro.api.exceptions

import io.github.pavelshe11.messengermicro.api.dto.ErrorDto
import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class CustomExceptionController(
    private val messageSource: MessageSource,
) {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ServerAnswerException::class)
    fun handleServerAnswerException(): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    @ExceptionHandler(FieldValidationException::class)
    fun handleFieldValidationException(ex: FieldValidationException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            error = getMessage("validation.error"),
            detailedErrors = ex.errors.toList()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonMappingException(ex: HttpMessageNotReadableException?): ResponseEntity<ErrorDto> {
        log.error(
            "Произошла ошибка json запроса: ",
            ex
        )

        val errorDto = ErrorDto(
            error = getMessage("validation.error"),
            detailedErrors = listOf(
                FieldErrorDto(
                    field = null,
                    message = getMessage("request.invalid")
                )
            )
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto)
    }

    @ExceptionHandler(ChatNotFoundException::class)
    fun handleChatNotFoundException(ex: ChatNotFoundException): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            error = getMessage("handle.error"),
            detailedErrors = ex.chatIds.map { chatId ->
                FieldErrorDto(
                    field = "chatsIdsToDeleting",
                    message = getMessage("chat.not.found"),
                    objectId = chatId
                )
            }
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto)
    }

    private fun getMessage(code: String, args: Array<Any>? = null): String =
        messageSource.getMessage(code, args, Locale.getDefault())

    companion object {
        private val log = LoggerFactory.getLogger(CustomExceptionController::class.java)
    }
}