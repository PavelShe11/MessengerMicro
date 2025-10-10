package io.github.pavelshe11.messengermicro.store.enums

enum class CursorDestinationType(val messageStatusType: String) {
    BEFORE("before"),
    AFTER("after");

    val isBefore: Boolean
        get() = this == BEFORE

    val isAfter: Boolean
        get() = this == AFTER

}