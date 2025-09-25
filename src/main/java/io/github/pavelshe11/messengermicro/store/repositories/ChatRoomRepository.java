package io.github.pavelshe11.messengermicro.store.repositories;

import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, UUID> {
}
