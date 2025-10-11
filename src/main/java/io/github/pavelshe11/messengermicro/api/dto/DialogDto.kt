package io.github.pavelshe11.messengermicro.api.dto

import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import java.time.Instant
import java.util.UUID

data class DialogDto(
    val chatRoomId: UUID,
    val lastMessage: MessageDto?
)

data class MessageDto(
    val id: UUID,
    val text: String,
    val senderId: UUID,
    val chatId: UUID,
    val sendingTime: Instant,
    val status: MessageStatusType,
    val isDraft: Boolean,
    val replyToMessageId: UUID?,
)