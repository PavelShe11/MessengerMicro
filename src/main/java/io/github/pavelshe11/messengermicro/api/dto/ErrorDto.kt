package io.github.pavelshe11.messengermicro.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ ошибки валидации полей")
data class ErrorDto(
    @Schema(description = "Общее название ошибки для отображения пользователю")
    val error: String? = null,

    @Schema(description = "Список ошибок")
    val detailedErrors: List<FieldErrorDto>? = null
)
