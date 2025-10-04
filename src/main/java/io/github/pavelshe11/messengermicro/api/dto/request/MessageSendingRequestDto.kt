package io.github.pavelshe11.messengermicro.api.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Запрос на отправление сообщения")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MessageSendingRequestDto(
    @Schema(description = "ID чата, в котором отправлено сообщение")
    val chatRoomId: UUID?,
    @Schema(description = "ID отправителя сообщения")
    val chatSenderId: UUID?,
    @Schema(description = "Содержание сообщения")
    val messageText: String,
    @Schema(description = "Статус сообщения")
    val messageStatusType: MessageStatusType,
    @Schema(description = "ID сообщения, на которое отвечает текущее сообщение. Может быть null")
    val parentMessageId: UUID?,

    ) {
}