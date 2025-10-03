package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "participant_type")
data class ParticipantTypeEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(name = "type_name", nullable = false)
    var typeName: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "participantType", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var participants: List<ParticipantEntity> = emptyList()
)
