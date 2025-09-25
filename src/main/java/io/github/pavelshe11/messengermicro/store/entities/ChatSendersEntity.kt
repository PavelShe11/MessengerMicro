package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "chat_senders")
data class ChatSendersEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
    var chatRoom: ChatRoomEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
    var participant: ParticipantEntity? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "chatSenders", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val messages: List<MessageEntity>? = null
)
