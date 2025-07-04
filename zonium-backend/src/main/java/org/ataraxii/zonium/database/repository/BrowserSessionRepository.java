package org.ataraxii.zonium.database.repository;

import org.ataraxii.zonium.database.entity.BrowserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrowserSessionRepository extends JpaRepository<BrowserSession, Long> {
}
