package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto

interface MessageService {
    fun sendMessage(request: MessageSendingRequestDto)
    fun deleteMessage(request: MessageDeletingRequestDto)
}