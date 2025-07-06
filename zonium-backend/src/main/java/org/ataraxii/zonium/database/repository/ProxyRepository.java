package org.ataraxii.zonium.database.repository;

import org.ataraxii.zonium.database.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {
    List<Proxy> findAllByUserId(UUID userId);
    Optional<Proxy> findByIdAndUserId(Long id, UUID userId);
}
