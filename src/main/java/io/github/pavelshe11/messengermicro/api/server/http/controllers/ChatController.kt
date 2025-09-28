package io.github.pavelshe11.messengermicro.api.server.http.controllers

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.response.ChatCreationResponseDto
import io.github.pavelshe11.messengermicro.services.ChatService
import io.github.pavelshe11.messengermicro.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@Tag(name = "Управление чатами", description = "API для работы с чатами")
@SecurityRequirement(name = "bearerTokenAuth")
@RequestMapping("/messenger/v1/chat")
class ChatController(
    private val chatService: ChatService,
    private val jwtUtil: JwtUtil
) {

    @Operation(summary = "Метод созадния чата участникам и их типам")
    @ApiResponse(responseCode = "200", description = "Чат успешно создан")
    @PostMapping(value = [""], produces = ["application/json"])
    fun createChat(
        @RequestBody request: ChatCreationRequestDto
    ): ResponseEntity<ChatCreationResponseDto> {
        val accountId = jwtUtil.claimAccountId();
        val response = chatService.createChat(request, accountId)
        log.info("Метод создания чата завершен")
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Метод удаления переданного списка ID чатов")
    @ApiResponse(responseCode = "200", description = "Чаты успешно удалены")
    @DeleteMapping(value = [""], produces = ["application/json"])
    fun deleteChats(
        @RequestBody request: ChatDeletingRequestDto
    ): ResponseEntity<Void> {
//        UUID accountId = jwtUtil.claimAccountId();
        chatService.deleteChats(request);
        log.info("Метод удаления чата завершен")
        return ResponseEntity.ok().build()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatController::class.java)
    }
}
