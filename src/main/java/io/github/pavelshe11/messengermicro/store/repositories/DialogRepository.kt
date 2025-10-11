package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface DialogRepository : JpaRepository<ChatRoomEntity, UUID> {

    @Query(
        nativeQuery = true,
        value = """
        SELECT 
            cr.id as chat_room_id,
            last_msg.id as message_id,
            last_msg.message_text as last_message_text,
            last_msg.sending_time as sending_time,
            last_msg.status as status,
            last_msg.draft as is_draft,
            cs.participant_id as sender_id,
            last_msg.parent_message_id as reply_to_message_id
        FROM chat_room cr
        INNER JOIN chat_senders cs ON cs.chat_id = cr.id
        INNER JOIN participant p ON p.id = cs.participant_id
        LEFT JOIN LATERAL (
            SELECT
                m.id,
                m.message_text, 
                m.sending_time,
                m.status,
                m.draft,
                m.parent_message_id
            FROM message m
            WHERE m.chat_id = cr.id
            ORDER BY m.sending_time DESC
            LIMIT 1
        ) last_msg ON true
        WHERE p.ref_id = :accountId
        AND (:cursorTime IS NULL OR 
             COALESCE(last_msg.sending_time, cr.created_at) < to_timestamp(:cursorTime / 1000.0)
             OR (COALESCE(last_msg.sending_time, cr.created_at) = to_timestamp(:cursorTime / 1000.0) 
                 AND cr.id < :cursorChatRoomId))
        ORDER BY COALESCE(last_msg.sending_time, cr.created_at) DESC, cr.id DESC 
        LIMIT :limit
        """
    )
    fun getAllDialogsWithPaginationAfter(
        accountId: UUID,
        cursorTime: Long?,
        cursorChatRoomId: UUID?,
        limit: Int
    ): List<Array<Any>>

    @Query(
        nativeQuery = true,
        value = """
        SELECT 
            cr.id as chat_room_id,
            last_msg.id as message_id,
            last_msg.message_text as last_message_text,
            last_msg.sending_time as sending_time,
            last_msg.status as status,
            last_msg.draft as is_draft,
            cs.participant_id as sender_id,
            last_msg.parent_message_id as reply_to_message_id
        FROM chat_room cr
        INNER JOIN chat_senders cs ON cs.chat_id = cr.id
        INNER JOIN participant p ON p.id = cs.participant_id
        LEFT JOIN LATERAL (
            SELECT
                m.id,
                m.message_text, 
                m.sending_time,
                m.status,
                m.draft,
                m.parent_message_id
            FROM message m
            WHERE m.chat_id = cr.id
            ORDER BY m.sending_time DESC
            LIMIT 1
        ) last_msg ON true
        WHERE p.ref_id = :accountId
        AND (:cursorTime IS NULL OR 
             COALESCE(last_msg.sending_time, cr.created_at) > to_timestamp(:cursorTime / 1000.0)
             OR (COALESCE(last_msg.sending_time, cr.created_at) = to_timestamp(:cursorTime / 1000.0)
                 AND cr.id > :cursorChatRoomId))
        ORDER BY COALESCE(last_msg.sending_time, cr.created_at) DESC, cr.id DESC
        LIMIT :limit
        """
    )
    fun getAllDialogsWithPaginationBefore(
        accountId: UUID,
        cursorTime: Long?,
        cursorChatRoomId: UUID?,
        limit: Int
    ): List<Array<Any>>
}