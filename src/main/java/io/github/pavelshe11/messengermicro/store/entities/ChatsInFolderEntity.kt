package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "chats_in_folder")
class ChatsInFolderEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
     var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
     var chatRoom: ChatRoomEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chats_folder_id", referencedColumnName = "id", nullable = false)
     var chatsFolder: ChatsFolderEntity? = null
)
