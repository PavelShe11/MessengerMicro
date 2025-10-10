package io.github.pavelshe11.messengermicro.store.entities

import java.time.Instant
import java.util.UUID

data class DialogDto(
    val chatRoomId: UUID,
    val title: String,
    val lastMessage: String,
    val lastMessageTime: Instant,
)
