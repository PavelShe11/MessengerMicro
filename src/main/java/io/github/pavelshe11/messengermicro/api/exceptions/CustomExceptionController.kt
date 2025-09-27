package io.github.pavelshe11.messengermicro.api.exceptions

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionController {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ServerAnswerException::class)
    fun handleServerAnswerException(): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    @ExceptionHandler(FieldValidationException::class)
    fun handleFieldValidationException(ex: FieldValidationException): ResponseEntity<Set<FieldErrorDto>> {
        return ResponseEntity(ex.errors, HttpStatus.BAD_REQUEST)
    }
}