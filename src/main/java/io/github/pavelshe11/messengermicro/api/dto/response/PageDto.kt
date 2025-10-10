package io.github.pavelshe11.messengermicro.api.dto.response

import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Базовый DTO для пагинации")
data class PageDto<T>(
    @Schema(description = "Список элементов")
    val content: List<T>,

    @Schema(description = "Курсор первого эелмента текущей страницы", nullable = true)
    val startCursor: String?,

    @Schema(description = "Курсор последнего элемента текущей страницы", nullable = true)
    val endCursor: String?,

    @Schema(description = "Есть ли предыдущая страница")
    val hasPreviousPage: Boolean = false,

    @Schema(description = "Есть ли следующая страница")
    val hasNextPage: Boolean = false,

    @Schema(description = "Запрошенный размер страницы")
    val size: Int = 0,
) {
    fun interface CursorCreator<T> {
        fun createCursor(lastItem: T): String?
    }

    companion object {
        fun <T> of(
            content: List<T>, requestedSize: Int, cursorCreator: CursorCreator<T>?,
            cursorDestinationType: CursorDestinationType, cursorName: String?,
            cursorId: UUID?
        ): PageDto<T> {
            val isFirstPage = cursorName == null && cursorId == null
            val hasExtraPage = content.size > requestedSize
            val actualContent: List<T>

            val hasNextPage: Boolean
            val hasPreviousPage: Boolean

            if (cursorDestinationType.isBefore) {
                hasNextPage = true
                hasPreviousPage = hasExtraPage
                actualContent =
                    if (hasExtraPage) content.subList(content.size - requestedSize, content.size) else content
                Collections.reverse(actualContent)
            } else {
                hasPreviousPage = !isFirstPage
                hasNextPage = hasExtraPage
                actualContent = if (hasExtraPage) content.subList(0, requestedSize) else content
            }

            val startCursor = if (actualContent.isNotEmpty() && cursorCreator != null)
                cursorCreator.createCursor(actualContent.first()) else null

            val endCursor = if (actualContent.isNotEmpty() && cursorCreator != null)
                cursorCreator.createCursor(actualContent.last()) else null

            return PageDto(
                content = actualContent,
                endCursor = endCursor,
                startCursor = startCursor,
                size = requestedSize,
                hasPreviousPage = hasPreviousPage,
                hasNextPage = hasNextPage
            )
        }

        fun encodeCursor(cursorData: String): String {
            return Base64.getEncoder().encodeToString(cursorData.toByteArray())
        }
    }
}
