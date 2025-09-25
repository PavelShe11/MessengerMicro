package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "archive_chat")
data class ArchiveChatEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
    val chatRoom: ChatRoomEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
    val participant: ParticipantEntity? = null,

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
)
