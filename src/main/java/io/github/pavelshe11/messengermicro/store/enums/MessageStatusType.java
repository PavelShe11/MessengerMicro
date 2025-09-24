package io.github.pavelshe11.messengermicro.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageStatusType {
    SENT("sent"),
    DELIVERED("delivered"),
    READ("read"),
    ERROR("error");

    private final String messageStatusType;
}
