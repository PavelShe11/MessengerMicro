package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "pinned_chat")
data class PinnedChatEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
     var chatRoom: ChatRoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
     var participant: ParticipantEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chats_folder_id", referencedColumnName = "id", nullable = false)
     var chatsFolder: ChatsFolderEntity,

    @Column(name = "added_at", nullable = false)
     var addedAt: Instant = Instant.now()
)
