package io.github.pavelshe11.messengermicro.api.server.http.controllers

import io.github.pavelshe11.messengermicro.annotations.CommonApiResponses
import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import io.github.pavelshe11.messengermicro.services.MessageService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Управление сообщениями", description = "API для работы с сообщениями")
@SecurityRequirement(name = "bearerTokenAuth")
@RequestMapping("/messenger/v1/message")
class MessageController(
    private val messageService: MessageService,
) {

    @PostMapping("/send")
    @CommonApiResponses
    fun sendMessage(
        request: MessageSendingRequestDto
    ): ResponseEntity<Void> {
        messageService.sendMessage(request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/delete")
    @CommonApiResponses
    fun deleteMessage(
        request: MessageDeletingRequestDto
    ): ResponseEntity<Void> {
        deleteMessage(request)
        return ResponseEntity.ok().build()
    }
}