package practice.springsecurity.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springsecurity.domain.entity.RoleHierarchy;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {

    RoleHierarchy findByChildName(String roleName);
}
