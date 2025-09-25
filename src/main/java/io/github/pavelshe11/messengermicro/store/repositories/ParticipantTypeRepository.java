package io.github.pavelshe11.messengermicro.store.repositories;

import io.github.pavelshe11.messengermicro.store.entities.ParticipantTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantTypeRepository extends JpaRepository<ParticipantTypeEntity, UUID> {
    Optional<ParticipantTypeEntity> findByTypeName(String type);
}
