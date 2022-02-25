package practice.springsecurity.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.springsecurity.domain.entity.RoleHierarchy;
import practice.springsecurity.domain.repository.RoleHierarchyRepository;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Transactional(readOnly = true)
    @Override
    public String findAllHierarchy() {

        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuilder concatedRoles = new StringBuilder();
        while (itr.hasNext()) {
            RoleHierarchy roleHierarchy = itr.next();
            if (roleHierarchy.getParentName() != null) {
                concatedRoles.append(roleHierarchy.getParentName().getChildName());
                concatedRoles.append(" > ");
                concatedRoles.append(roleHierarchy.getChildName());
                concatedRoles.append(System.lineSeparator());
            }
        }
        return concatedRoles.toString();

    }
}
