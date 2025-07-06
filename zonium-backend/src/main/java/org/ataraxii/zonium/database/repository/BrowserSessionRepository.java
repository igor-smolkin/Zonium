package org.ataraxii.zonium.database.repository;

import org.ataraxii.zonium.database.entity.BrowserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrowserSessionRepository extends JpaRepository<BrowserSession, Long> {
    List<BrowserSession> findAllByUserId(UUID userId);

    Optional<BrowserSession> findByIdAndUserId(Long id, UUID userId);
}
