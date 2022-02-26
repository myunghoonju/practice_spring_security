package practice.springsecurity.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import practice.springsecurity.domain.entity.Resources;
import practice.springsecurity.domain.repository.AccessIpRepository;
import practice.springsecurity.domain.repository.ResourcesRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SecurityResourceService {

    private final ResourcesRepository repository;
    private final AccessIpRepository ipRepository;

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

    public List<String> getAccessIpList() {
        return ipRepository.findAll().stream()
                .map(ip -> ip.getIpAddress())
                .collect(Collectors.toList());
    }
}
