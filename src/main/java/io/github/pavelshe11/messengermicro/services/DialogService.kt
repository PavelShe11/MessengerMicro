package io.github.pavelshe11.messengermicro.services

import io.github.pavelshe11.messengermicro.api.dto.response.DialogPageDto
import io.github.pavelshe11.messengermicro.store.enums.CursorDestinationType
import java.util.*

interface DialogService {
    fun getDialogs(accountId: UUID, keyword: String?, cursor: String?, pageSize: Int, cursorDestination: CursorDestinationType): DialogPageDto
}