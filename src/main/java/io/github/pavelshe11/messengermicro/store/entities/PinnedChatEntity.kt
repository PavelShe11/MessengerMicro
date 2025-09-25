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
     var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
     val chatRoom: ChatRoomEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
     val participant: ParticipantEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chats_folder_id", referencedColumnName = "id", nullable = false)
     var chatsFolder: ChatsFolderEntity? = null,

    @Column(name = "added_at", nullable = false)
     var addedAt: Instant = Instant.now()
)
