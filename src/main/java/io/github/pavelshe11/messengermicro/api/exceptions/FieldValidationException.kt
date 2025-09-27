package io.github.pavelshe11.messengermicro.api.exceptions

import io.github.pavelshe11.messengermicro.api.dto.FieldErrorDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class FieldValidationException(
    val errors: Set<FieldErrorDto>
) : RuntimeException("Ошибка валидации данных")