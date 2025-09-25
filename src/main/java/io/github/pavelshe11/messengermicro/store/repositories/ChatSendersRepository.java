package io.github.pavelshe11.messengermicro.store.repositories;

import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatSendersRepository extends JpaRepository<ChatSendersEntity, UUID> {
}
