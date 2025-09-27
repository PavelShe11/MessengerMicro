package io.github.pavelshe11.messengermicro.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Ответ ошибки")
data class FieldErrorDto(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:Schema(description = "Поле ошибки")
    private val field: String,

    @field:Schema(description = "Сообщение ошибки для отображения рядом с полем ввода")
    private val message: String
) {

    @Schema(description = "ID объекта, к которому относится ошибка")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val objectId: UUID? = null
}