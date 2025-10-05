package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import java.util.UUID

interface MessageService {
    fun sendMessage(request: MessageSendingRequestDto, accountId: UUID)
    fun deleteMessage(request: MessageDeletingRequestDto)
}