package io.github.pavelshe11.messengermicro.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(HttpStatus.NOT_FOUND)
class ChatNotFoundException(val chatIds: Set<UUID>) : RuntimeException()
