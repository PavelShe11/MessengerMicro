package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ChatSendersRepository : JpaRepository<ChatSendersEntity?, UUID?> {
    fun findAllByChatRoom(chatRoom: ChatRoomEntity): List<ChatSendersEntity>
    fun existsByParticipant(participant: ParticipantEntity): Boolean


    @Query(
        value = """
            SELECT chatSenders.chat_id
            FROM chat_senders chatSenders
            WHERE chatSenders.participant_id IN :participantIds
            GROUP BY chatSenders.chat_id
            HAVING COUNT(chatSenders.participant_id) = :participantCount
                AND COUNT(chatSenders.participant_id) = (
                SELECT COUNT(*) FROM chat_senders chatSendersItself 
                WHERE chatSenders.chat_id = chatSendersItself.chat_id
                )
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findExistingChat(
        @Param("participantIds") participantIds: Set<UUID>,
        @Param("participantCount") participantCount: Int
    ): Optional<UUID>
}
