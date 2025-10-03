package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ParticipantTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ParticipantTypeRepository : JpaRepository<ParticipantTypeEntity?, UUID?> {
    fun findByTypeName(type: String): ParticipantTypeEntity?
}
