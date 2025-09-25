package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "participant")
data class ParticipantEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_type_id", referencedColumnName = "id", nullable = false)
    var participantType: ParticipantTypeEntity? = null,

    @Column(name = "ref_id", nullable = false)
    var refId: UUID,

// For two-way communication with FK
    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val archiveChats: List<ArchiveChatEntity>? = null,

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val pinnedChats: List<PinnedChatEntity>? = null,

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val chatSenders: List<ChatSendersEntity>? = null,

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val chatsFolders: List<ChatsFolderEntity>? = null
)

