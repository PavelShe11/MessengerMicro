package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto

interface ChatService {
    fun createChat(request: ChatCreationRequestDto)
    fun deleteChats(request: ChatDeletingRequestDto)
}