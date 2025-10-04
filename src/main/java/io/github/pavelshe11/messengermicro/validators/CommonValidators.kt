package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import io.github.pavelshe11.messengermicro.api.exceptions.FieldValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class CommonValidators(
    private val messageSource: MessageSource
) {

    fun getMessage(code: String, args: Array<Any>? = null): String =
        messageSource.getMessage(code, args, Locale.getDefault())

    fun validateInputAndReturnOrThrow(vararg validators: () -> FieldErrorDto?) {
        val errors = validators.mapNotNull { it() }

        if (errors.isNotEmpty()) {
            errors.forEach { log.error("Validation error: field = ${it.field}, message = ${it.message}") }

            throw FieldValidationException(errors.toSet())
        }
    }

    fun validateNotBlank(value: String?, fieldName: String): String? {
        return if (value.isNullOrBlank()) "field.empty" else null
    }

    fun validateUUID(value: String, field: String): String? {
        return try {
            UUID.fromString(value)
            null
        } catch (ex: Exception) {
            log.error("Неверный UUID для поля: $field - $value", ex)
            "error.uuid.invalid"
        }
    }
    companion object {
        private val log: Logger = LoggerFactory.getLogger(CommonValidators::class.java)
    }
}