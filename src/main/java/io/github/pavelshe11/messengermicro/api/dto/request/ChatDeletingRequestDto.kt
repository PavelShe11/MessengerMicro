package io.github.pavelshe11.messengermicro.api.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Запрос на удаление чата")
data class ChatDeletingRequestDto(
    @Schema(description = "Список ID чатов для удаления")
    val chatsIdsToDeleting: List<UUID>? = null
)