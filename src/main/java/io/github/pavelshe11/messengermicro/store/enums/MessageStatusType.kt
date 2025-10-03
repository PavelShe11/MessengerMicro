package io.github.pavelshe11.messengermicro.store.enums

enum class MessageStatusType(val messageStatusType: String) {
    SENT("sent"),
    DELIVERED("delivered"),
    READ("read"),
    ERROR("error");
}
