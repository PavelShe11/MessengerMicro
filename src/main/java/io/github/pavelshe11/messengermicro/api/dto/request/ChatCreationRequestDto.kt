package io.github.pavelshe11.messengermicro.api.dto.request

import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "Запрос на создание чата")
data class ChatCreationRequestDto(
    @Schema(description = "Список участников чата")
    val participants: List<ParticipantDto>
) {
    @Schema(description = "Информация об участнике")
    data class ParticipantDto(
        @Schema(description = "Тип участника")
        val participantType: String,

        @Schema(description = "ID сущности из микросервиса")
        val refId: UUID
    )
}
