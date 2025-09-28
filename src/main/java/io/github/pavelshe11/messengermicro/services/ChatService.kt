package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.response.ChatCreationResponseDto
import java.util.*

interface ChatService {
    fun createChat(request: ChatCreationRequestDto, accountId: UUID): ChatCreationResponseDto
    fun deleteChats(request: ChatDeletingRequestDto)
}