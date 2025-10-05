package io.github.pavelshe11.messengermicro.normalizers

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class DataNormalizer {
    fun normalizeChatCreationRequest(request: ChatCreationRequestDto): ChatCreationRequestDto {
        val normalizedParticipants = request.participants.map { participant ->
            ChatCreationRequestDto.ParticipantDto(
                participantType = normalizeStrings(participant.participantType),
                refId = participant.refId
            )
        }
        return ChatCreationRequestDto(participants = normalizedParticipants)
    }

    fun normalizeMessageSendingRequest(request: MessageSendingRequestDto): MessageSendingRequestDto {
        return MessageSendingRequestDto(
            chatRoomId = request.chatRoomId,
            chatSenderId = request.chatSenderId,
            messageText = normalizeNullableString(request.messageText) ?: "",
            messageStatusType = request.messageStatusType,
            parentMessageId = request.parentMessageId,
        )
    }

    private fun normalizeStrings(inputData: String): String {
        return inputData.trim().lowercase(Locale.getDefault())
    }

    private fun normalizeNullableString(input: String?): String? {
        return input?.trim()?.takeIf { it.isNotEmpty() }
    }
}