package io.github.pavelshe11.messengermicro.store.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "participant")
data class ParticipantEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_type_id", referencedColumnName = "id", nullable = false)
    var participantType: ParticipantTypeEntity,

    @Column(name = "ref_id", nullable = false)
    var refId: UUID,

// For two-way communication with FK
    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var archiveChats: List<ArchiveChatEntity> = emptyList(),

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var pinnedChats: List<PinnedChatEntity> = emptyList(),

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var chatSenders: List<ChatSendersEntity> = emptyList(),

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var chatsFolders: List<ChatsFolderEntity> = emptyList()
)

