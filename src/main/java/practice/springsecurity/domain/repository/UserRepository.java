package practice.springsecurity.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springsecurity.domain.entity.Account;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    int countByUsername(String username);
}
