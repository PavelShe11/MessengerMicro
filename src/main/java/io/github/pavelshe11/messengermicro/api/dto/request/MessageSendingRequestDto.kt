package io.github.pavelshe11.messengermicro.api.dto.request

import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import java.util.UUID

data class MessageSendingRequestDto(
    val chatRoomId: UUID?,
    val chatSenderId: UUID?,
    val messageText: String,
    val messageStatusType: MessageStatusType,
    val parentMessageId: UUID?,

    ) {
}