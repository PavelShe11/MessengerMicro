package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import java.util.*

interface ChatService {
    fun createChat(request: ChatCreationRequestDto, accountId: UUID)
    fun deleteChats(request: ChatDeletingRequestDto)
}