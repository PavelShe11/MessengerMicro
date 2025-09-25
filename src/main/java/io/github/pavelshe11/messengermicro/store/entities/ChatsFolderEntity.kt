package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "chats_folder")
class ChatsFolderEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
     var id: UUID? = null,

    @Column(name = "folder_name", nullable = false)
     var folderName: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
     val participant: ParticipantEntity? = null,

    @Column(name = "created_at", nullable = false)
     val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
     var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "chatsFolder", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val chatsInFolders: List<ChatsInFolderEntity>? = null,

    @OneToMany(mappedBy = "chatsFolder", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val pinnedChats: List<PinnedChatEntity>? = null
)
