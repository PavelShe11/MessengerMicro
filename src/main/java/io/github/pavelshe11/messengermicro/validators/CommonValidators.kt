package io.github.pavelshe11.messengermicro.validators

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class CommonValidators {

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