package io.github.pavelshe11.messengermicro.api.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Запрос на удаление сообщения")
data class MessageDeletingRequestDto (
    @Schema(description = "Список ID сообщений для удаления")
    val messagesIdsToDeleting: List<UUID>? = null
)