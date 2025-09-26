package io.github.pavelshe11.messengermicro.api.server.http.controllers

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.services.ChatService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Управление чатами", description = "API для работы с чатами")
@SecurityRequirement(name = "bearerTokenAuth")
@RequestMapping("/messenger/v1/chat")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping(value = [""], produces = ["application/json"])
    fun createChat(
        @RequestBody request: ChatCreationRequestDto
    ): ResponseEntity<Void> {
        //TODO: create chat
//        UUID accountId = jwtUtil.claimAccountId();
        chatService.createChat(request)
        log.info("Чат созадн")
        return ResponseEntity.ok().build()
    }

    @DeleteMapping(value = [""], produces = ["application/json"])
    fun deleteChats(
        @RequestBody request: ChatDeletingRequestDto?
    ): ResponseEntity<Void> {
        //TODO: create chat
//        UUID accountId = jwtUtil.claimAccountId();
//        chatService.deleteChats(request);
        log.info("Чат удален")
        return ResponseEntity.ok().build()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatController::class.java)
    }
}
