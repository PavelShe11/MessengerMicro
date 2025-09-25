package io.github.pavelshe11.messengermicro.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на удаление чата")
public class ChatDeletingRequestDto {

    @Schema(description = "Список ID чатов для удаления")
    private List<UUID> chatsIdsToDeleting;
}
