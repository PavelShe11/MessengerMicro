package io.github.pavelshe11.messengermicro.api.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Ответ на создание чата")
data class ChatCreationResponseDto(
    @Schema(description = "ID созданного или найденного чата")
    val chatId: UUID
)
