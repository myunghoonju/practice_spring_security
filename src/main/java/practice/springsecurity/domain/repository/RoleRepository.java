package practice.springsecurity.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springsecurity.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);
}
