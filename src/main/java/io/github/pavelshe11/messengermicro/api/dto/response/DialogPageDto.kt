package io.github.pavelshe11.messengermicro.api.dto.response

import io.github.pavelshe11.messengermicro.api.dto.DialogDto
import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import java.util.*

data class DialogPageDto(
    val content: List<DialogDto>,
    val startCursor: String,
    val endCursor: String,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val size: Int,

    ) {
    companion object {
        fun ofByLastMessage(
            content: List<DialogDto>,
            requestedSize: Int,
            cursorDestinationType: CursorDestinationType,
            cursorTime: Long?,
            cursorId: UUID?
        ): DialogPageDto {
            val page = PageDto.of(
                content,
                requestedSize,
                cursorCreator = { item ->
                    PageDto.encodeCursor("${item.lastMessage?.sendingTime?.toEpochMilli()}|${item.chatRoomId}")
                },
                cursorDestinationType = cursorDestinationType,
                cursorName = cursorTime?.toString(),
                cursorId = cursorId
            )
            return DialogPageDto(
                content = page.content,
                startCursor = page.startCursor ?: "",
                endCursor = page.endCursor ?: "",
                hasNextPage = page.hasNextPage,
                hasPreviousPage = page.hasPreviousPage,
                size = page.size,

                )
        }
    }
}
