package io.github.pavelshe11.messengermicro.api.dto.request;

import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание чата")
public class ChatCreationRequestDto {

    @Schema(description = "Список участников чата")
    List<ParticipantDto> participants;

    @Data
    @AllArgsConstructor
    @Builder
    @Schema(description = "Информация об участнике")
    public static class ParticipantDto {
        @Schema(description = "Тип участника")
        private String type;

        @Schema(description = "ID участника")
        private ParticipantEntity participant;
    }

}
