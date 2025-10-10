package io.github.pavelshe11.messengermicro.api.server.http.controllers

import io.github.pavelshe11.messengermicro.annotations.CommonApiResponses
import io.github.pavelshe11.messengermicro.api.dto.ErrorDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageDeletingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.request.MessageSendingRequestDto
import io.github.pavelshe11.messengermicro.api.dto.response.DialogPageDto
import io.github.pavelshe11.messengermicro.services.MessageService
import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import io.github.pavelshe11.messengermicro.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Управление сообщениями", description = "API для работы с сообщениями")
@SecurityRequirement(name = "bearerTokenAuth")
@RequestMapping("/messenger/v1/message")
class MessageController(
    private val messageService: MessageService,
    private val jwtUtil: JwtUtil,
) {

    @Operation(summary = "Метод отправки сообщения")
    @ApiResponse(responseCode = "200", description = "Сообщение успешно отправлено")
    @ApiResponse(
        responseCode = "400",
        description = "Ошибка валидации данных",
        content = [Content(schema = Schema(implementation = ErrorDto::class))]
    )
    @CommonApiResponses
    @PostMapping(value=["/send"], produces = ["application/json"])
    fun sendMessage(
        request: MessageSendingRequestDto
    ): ResponseEntity<Void> {
        val accountId = jwtUtil.claimAccountId();
        messageService.sendMessage(request, accountId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Метод удаления сообщений по ID")
    @ApiResponse(responseCode = "200", description = "Сообщения успешно удалены")
    @ApiResponse(
        responseCode = "404",
        description = "Сообщение не найдено. Возможная причина: MessageNotFoundException",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorDto::class)
            )
        ]
    )
    @CommonApiResponses
    @DeleteMapping(value = ["/delete"], produces = ["application/json"])
    fun deleteMessage(
        @RequestBody request: MessageDeletingRequestDto
    ): ResponseEntity<Void> {
        messageService.deleteMessage(request)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Метод получения списка диалогов с последним сообщением по ID пользователя")
    @ApiResponse(responseCode = "200", description = "Диалоги успешно получены")
    @CommonApiResponses
    @GetMapping(value = ["/dialogs"], produces = ["application/json"])
    fun getDialogs(
        @Parameter(description = "Фильтр по имени пользователя или чату", example = "Альберт")
        @RequestParam(required = false) keyword: String? = null,
        @Parameter(description = "Курсор для постраничного вывода (Base64 строка)", example = "eyJpZCI6IjEyMyJ9")
        @RequestParam(required = false) cursor: String? = null,
        @Parameter(description = "Выбор элементов до или после курсора (before/after)")
        @RequestParam(required = false) cursorDestination: CursorDestinationType? = null,
        @Parameter(description = "Количество элементов на страницу")
        @RequestParam(name = "size", defaultValue = "10") pageSize: Int = 10
    ): DialogPageDto {
        val accountId = jwtUtil.claimAccountId()
        val dialogs = messageService.getDialogsByKeyword(accountId, keyword, cursor, pageSize, cursorDestination)
        return dialogs
    }
}