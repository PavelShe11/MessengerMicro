package io.github.pavelshe11.messengermicro.store.entities

import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "message")
data class MessageEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
     var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
     val chatRoom: ChatRoomEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
     val chatSenders: ChatSendersEntity? = null,

    @Column(name = "message_text")
     var messageText: String? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
     var messageStatusType: MessageStatusType? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
     val parentMessage: MessageEntity? = null,

    @OneToMany(mappedBy = "parentMessage", cascade = [CascadeType.ALL], orphanRemoval = true)
     val replies: List<MessageEntity>? = null
)
