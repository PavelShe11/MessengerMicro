package io.github.pavelshe11.messengermicro.store.entities

import io.github.pavelshe11.messengermicro.store.enums.MessageStatusType
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "message")
data class MessageEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
     var chatRoom: ChatRoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
     var chatSenders: ChatSendersEntity,

    @Column(name = "message_text", nullable = false)
     var messageText: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
     var messageStatusType: MessageStatusType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    var parentMessage: MessageEntity? = null,

    @Column(nullable = false)
    var draft: Boolean = false,

    @Column(name = "sending_time", nullable = false)
    var sendingTime: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "parentMessage", cascade = [CascadeType.ALL], orphanRemoval = true)
     var replies: List<MessageEntity> = emptyList()
)
