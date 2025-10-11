package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.DialogDto
import io.github.pavelshe11.messengermicro.api.dto.MessageDto
import io.github.pavelshe11.messengermicro.api.dto.response.DialogPageDto
import io.github.pavelshe11.messengermicro.api.exceptions.ServerAnswerException
import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import io.github.pavelshe11.messengermicro.store.repositories.DialogRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DialogServiceImpl(
    private val dialogRepository: DialogRepository,

    ) : DialogService {

    override fun getDialogs(
        accountId: UUID,
        keyword: String?,
        cursor: String?,
        pageSize: Int,
        cursorDestination: CursorDestinationType
    ): DialogPageDto {

        var cursorTime: Long? = null
        var cursorChatRoomId: UUID? = null

        if (!cursor.isNullOrEmpty()) {
            try {
                log.info("Получен cursor: [{}]", cursor)
                val decoded = String(Base64.getDecoder().decode(cursor))
                log.info("Декодированный cursor: [{}]", decoded)
                val parts = decoded.split("\\|".toRegex(), limit = 2).toTypedArray()
                cursorTime = parts[0].toLong()
                cursorChatRoomId = UUID.fromString(parts[1])
                log.info(
                    "Парсинг курсора прошёл успешно. cursorTime: [{}], cursorChatRoomId: [{}]",
                    cursorTime, cursorChatRoomId
                )
            } catch (e: IllegalArgumentException) {
                log.warn("Ошибка при разборе параметра cursor: [{}]", cursor, e)
                throw ServerAnswerException()
            } catch (e: ArrayIndexOutOfBoundsException) {
                log.warn("Ошибка при разборе параметра cursor: [{}]", cursor, e)
                throw ServerAnswerException()
            }
        }

        val normalizedKeyword = keyword?.trim()?.lowercase(Locale.getDefault()) ?: ""

        val rows: List<Array<Any>> =
            processSearch(
                accountId = accountId,
                keyword = normalizedKeyword,
                cursorTime = cursorTime,
                cursorChatRoomId = cursorChatRoomId,
                pageSize = pageSize + 1,
                cursorDestinationType = cursorDestination
            )

        // Запись в DTO. Порядок совпадает с select выборкой и порядком полей в DTO.
        val content: List<DialogDto> = rows.map { row ->

            val chatRoomId = row[0] as UUID
            val messageId = row[1] as? UUID

            val lastMessage = if (messageId != null) {
                MessageDto(
                    id = messageId,
                    text = row[2] as String,
                    sendingTime = row[3] as Instant,
                    status = MessageStatusType.valueOf(row[4] as String),
                    isDraft = row[5] as Boolean,
                    senderId = row[6] as UUID,
                    chatId = chatRoomId,
                    replyToMessageId = row[7] as? UUID
                )
            } else {
                null
            }
            DialogDto(
                chatRoomId = chatRoomId,
                lastMessage = lastMessage
            )
        }

        return DialogPageDto.ofByLastMessage(
            content = content,
            requestedSize = pageSize,
            cursorDestinationType = cursorDestination,
            cursorTime = cursorTime,
            cursorId = cursorChatRoomId
        )
    }

    private fun processSearch(
        accountId: UUID,
        keyword: String,
        cursorTime: Long?,
        cursorChatRoomId: UUID?,
        pageSize: Int,
        cursorDestinationType: CursorDestinationType
    ): List<Array<Any>> {
        return if (cursorDestinationType.isAfter) {
            dialogRepository.getAllDialogsWithPaginationAfter(
                accountId = accountId,
                cursorTime = cursorTime,
                cursorChatRoomId = cursorChatRoomId,
                limit = pageSize
            )
        } else {
            dialogRepository.getAllDialogsWithPaginationAfter(
                accountId = accountId,
                cursorTime = cursorTime,
                cursorChatRoomId = cursorChatRoomId,
                limit = pageSize
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}