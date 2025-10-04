package io.github.pavelshe11.messengermicro.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class MessageNotFoundException(val messagesIds: Set<UUID>) : RuntimeException() {
}