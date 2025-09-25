package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "chat_room")
data class ChatRoomEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
     var id: UUID? = null,

    @Column(name = "created_at", nullable = false)
     val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
     var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val messages: List<MessageEntity>? = null,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val chatSenders: List<ChatSendersEntity>? = null,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val chatsInFolder: List<ChatsInFolderEntity>? = null,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val archiveChats: List<ArchiveChatEntity>? = null,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     val pinnedChats: List<PinnedChatEntity>? = null
)
