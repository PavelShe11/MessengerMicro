package io.github.pavelshe11.messengermicro.api.server.http.controllers

import io.github.pavelshe11.messengermicro.annotations.CommonApiResponses
import io.github.pavelshe11.messengermicro.api.dto.response.DialogPageDto
import io.github.pavelshe11.messengermicro.services.DialogService
import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import io.github.pavelshe11.messengermicro.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Управление диалогами", description = "API для работы с диалогами")
@SecurityRequirement(name = "bearerTokenAuth")
@RequestMapping("/messenger/v1/dialog")
class DialogController(
    private val jwtUtil: JwtUtil,
    private val dialogService: DialogService,

    ) {

    @Operation(summary = "Метод получения списка диалогов с последним сообщением по ID пользователя")
    @ApiResponse(responseCode = "200", description = "Диалоги успешно получены")
    @CommonApiResponses
    @GetMapping(value = ["/dialogs"], produces = ["application/json"])
    fun getDialogs(
//        @Parameter(description = "Фильтр по имени пользователя или чату", example = "Альберт")
//        @RequestParam(required = false) keyword: String? = null,
        @Parameter(description = "Курсор для постраничного вывода (Base64 строка)", example = "eyJpZCI6IjEyMyJ9")
        @RequestParam(required = false) cursor: String? = null,
        @Parameter(description = "Выбор элементов до или после курсора (before/after)")
        @RequestParam(required = false, defaultValue = "after") cursorDestination: CursorDestinationType
        = CursorDestinationType.AFTER,
        @Parameter(description = "Количество элементов на страницу")
        @RequestParam(name = "size", defaultValue = "10") pageSize: Int = 10
    ): DialogPageDto {
        val accountId = jwtUtil.claimAccountId()
        val dialogs = dialogService.getDialogs(accountId, keyword = null, cursor, pageSize, cursorDestination)
        return dialogs
    }
}