package practice.springsecurity.domain.service;

import practice.springsecurity.domain.entity.Account;
import practice.springsecurity.domain.entity.AccountDto;

import java.util.List;

public interface UserService {

    void createUser(Account account);

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long idx);

    void order();
}
