package org.ataraxii.zoniumauth.database.repository;

import org.ataraxii.zoniumauth.database.entity.Role;
import org.ataraxii.zoniumauth.database.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleType name);
}
