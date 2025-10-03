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
    val id: UUID? = null,

    @Column(name = "created_at", nullable = false)
     val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
     var updatedAt: Instant = Instant.now(),

    // For two-way communication with FK
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var messages: List<MessageEntity> = emptyList(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var chatSenders: List<ChatSendersEntity> = emptyList(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var chatsInFolder: List<ChatsInFolderEntity> = emptyList(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var archiveChats: List<ArchiveChatEntity> = emptyList(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
     var pinnedChats: List<PinnedChatEntity> = emptyList()
)
