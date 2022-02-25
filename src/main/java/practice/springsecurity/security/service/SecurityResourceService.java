package practice.springsecurity.security.service;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import practice.springsecurity.domain.entity.Resources;
import practice.springsecurity.domain.repository.ResourcesRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SecurityResourceService {

    private ResourcesRepository repository;

    public SecurityResourceService(ResourcesRepository repository) {
        this.repository = repository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        List<Resources> resourcesList = repository.findAllResources();
        resourcesList.forEach(resources -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();

            resources.getRoleSet().forEach(
                    role -> {
                        configAttributes.add(new SecurityConfig(role.getRoleName()));
                        result.put(new AntPathRequestMatcher(resources.getResourceName()), configAttributes);
                    });
        });

        return result;
    }
}
