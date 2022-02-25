package practice.springsecurity.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.springsecurity.domain.repository.ResourcesRepository;
import practice.springsecurity.security.service.SecurityResourceService;

@Configuration
public class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(ResourcesRepository repository) {
        return new SecurityResourceService(repository);
    }
}
