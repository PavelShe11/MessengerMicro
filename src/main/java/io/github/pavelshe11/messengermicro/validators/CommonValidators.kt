package io.github.pavelshe11.messengermicro.validators

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import org.springframework.stereotype.Component

@Component
class CommonValidators {

    fun validateNotBlank(value: String, field: String): FieldErrorDto? {
        return if (value.isBlank()) {
            FieldErrorDto(field, "Поле не должно быть пустым")
        } else null
    }
}