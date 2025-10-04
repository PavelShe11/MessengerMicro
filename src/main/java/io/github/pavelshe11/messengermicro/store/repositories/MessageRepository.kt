package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MessageRepository : JpaRepository<MessageEntity, UUID> {
}