package org.microservicesrevision.user.repo;

import org.microservicesrevision.user.model.Role;
import org.microservicesrevision.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
