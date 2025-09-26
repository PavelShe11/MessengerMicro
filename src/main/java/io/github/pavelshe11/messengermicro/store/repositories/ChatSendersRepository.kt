package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity
import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatSendersRepository : JpaRepository<ChatSendersEntity?, UUID?> {
    fun findAllByChatRoom(chatRoom: ChatRoomEntity): List<ChatSendersEntity>
    fun existsByParticipant(participant: ParticipantEntity): Boolean
}
