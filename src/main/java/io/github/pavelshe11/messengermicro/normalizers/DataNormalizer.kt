package io.github.pavelshe11.messengermicro.normalizers

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
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

    private fun normalizeStrings(inputData: String): String {
        return inputData.trim().lowercase(Locale.getDefault())
    }
}