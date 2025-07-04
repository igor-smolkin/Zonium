package org.ataraxii.zonium.database.repository;

import org.ataraxii.zonium.database.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {
}
