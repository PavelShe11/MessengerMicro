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
    val id: UUID? = null,

    @Column(name = "folder_name", nullable = false)
     var folderName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
     var participant: ParticipantEntity,

    @Column(name = "created_at", nullable = false)
     val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
     var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "chatsFolder", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var chatsInFolders: List<ChatsInFolderEntity>,

    @OneToMany(mappedBy = "chatsFolder", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var pinnedChats: List<PinnedChatEntity>
)
