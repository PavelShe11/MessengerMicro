package io.github.pavelshe11.messengermicro.store.repositories

import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ParticipantRepository : JpaRepository<ParticipantEntity?, UUID?> {
    fun findByRefId(refId: UUID): ParticipantEntity?
}
