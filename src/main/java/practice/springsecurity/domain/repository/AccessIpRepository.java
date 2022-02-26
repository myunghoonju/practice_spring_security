package practice.springsecurity.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springsecurity.domain.entity.AccessIp;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {

    AccessIp findByIpAddress(String IpAddress);
}