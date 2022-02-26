package practice.springsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import practice.springsecurity.domain.entity.AccessIp;
import practice.springsecurity.domain.entity.Account;
import practice.springsecurity.domain.entity.Resources;
import practice.springsecurity.domain.entity.Role;
import practice.springsecurity.domain.entity.RoleHierarchy;
import practice.springsecurity.domain.repository.AccessIpRepository;
import practice.springsecurity.domain.repository.ResourcesRepository;
import practice.springsecurity.domain.repository.RoleHierarchyRepository;
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
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final AccessIpRepository accessIpRepository;
    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
        setupAccessIpData();

        alreadySetup = true;
    }

    private void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Set<Role> roles1 = new HashSet<>();
        Set<Role> roles3 = new HashSet<>();

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        Role userRole = createRoleIfNotFound("ROLE_USER", "회원");

        roles.add(adminRole);
        roles1.add(managerRole);
        roles3.add(userRole);

        createUserIfNotFound("admin", "pass", "admin@gmail.com", 10,  roles);
        createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);
        createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);

        createResourceIfNotFound("/admin/**", "", roles, "url");
        createResourceIfNotFound("/mypage", "", roles3, "url");
        createResourceIfNotFound("/messages", "", roles1, "url");
        createResourceIfNotFound("/config", "", roles, "url");
        createResourceIfNotFound("practice.springsecurity.domain.service", "", roles1, "method");

        createRoleHierarchyIfNotFound(userRole, managerRole);
        createRoleHierarchyIfNotFound(managerRole, adminRole);
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
    public void createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {
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

        userRepository.save(account);
    }

    @Transactional
    public void createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
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

        resourcesRepository.save(resources);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {

        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }

    private void setupAccessIpData() {
        AccessIp ip1 = AccessIp.builder()
                .ipAddress("0:0:0:0:0:0:0:1")
                .build();
        AccessIp ip2 = AccessIp.builder()
                .ipAddress("127.0.0.1")
                .build();

        AccessIp ip1Addr = accessIpRepository.findByIpAddress(ip1.getIpAddress());
        AccessIp ip2Addr = accessIpRepository.findByIpAddress(ip2.getIpAddress());
        if (ObjectUtils.isEmpty(ip1Addr)) {
            accessIpRepository.save(ip1);
        }
        if (ObjectUtils.isEmpty(ip2Addr)) {
            accessIpRepository.save(ip2);
        }
    }
}
