package practice.springsecurity.security.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import practice.springsecurity.domain.entity.Account;

import java.util.List;

@Getter
@Setter
public class AccountContext extends User {
  private Account account;

  public AccountContext(Account account, List<GrantedAuthority> roles) {
    super(account.getUsername(), account.getPassword(), roles);
    this.account = account;
  }
}
