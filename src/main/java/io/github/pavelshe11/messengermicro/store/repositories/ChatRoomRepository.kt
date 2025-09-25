package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRoomRepository : JpaRepository<ChatRoomEntity?, UUID?>
