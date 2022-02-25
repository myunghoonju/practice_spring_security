package practice.springsecurity.security.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.springsecurity.domain.entity.Account;
import practice.springsecurity.domain.entity.Resources;
import practice.springsecurity.domain.entity.Role;
import practice.springsecurity.domain.repository.ResourcesRepository;
import practice.springsecurity.domain.repository.RoleRepository;
import practice.springsecurity.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final PasswordEncoder passwordEncoder;
    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();

        alreadySetup = true;
    }

    private void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Set<Role> roles1 = new HashSet<>();
        Set<Role> roles3 = new HashSet<>();

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        Role childRole1 = createRoleIfNotFound("ROLE_USER", "회원");

        roles.add(adminRole);
        roles1.add(managerRole);
        roles3.add(childRole1);

        createUserIfNotFound("admin", "pass", "admin@gmail.com", 10,  roles);
        createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);
        createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);

        createResourceIfNotFound("/admin/**", "", roles, "url");
        createResourceIfNotFound("/mypage", "", roles, "url");
        createResourceIfNotFound("/messages", "", roles, "url");
        createResourceIfNotFound("/config", "", roles, "url");

//        createResourceIfNotFound("io.security.corespringsecurity.aopsecurity.method.AopMethodService.methodTest", "", roles1, "method");
//        createResourceIfNotFound("io.security.corespringsecurity.aopsecurity.method.AopMethodService.innerCallMethodTest", "", roles1, "method");
//        createResourceIfNotFound("execution(* io.security.corespringsecurity.aopsecurity.pointcut.*Service.*(..))", "", roles1, "pointcut");
//        createResourceIfNotFound("/users/**", "", roles3, "url");
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {

        Account account = userRepository.findByUsername(userName);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .email(email)
                    .age(age)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        return userRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .httpMethod(httpMethod)
                    .orderNum(count.incrementAndGet())
                    .resourceType(resourceType)
                    .roleSet(roleSet)
                    .build();
        }
        return resourcesRepository.save(resources);
    }
}
