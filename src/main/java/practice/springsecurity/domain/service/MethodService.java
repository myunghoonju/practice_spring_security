package practice.springsecurity.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MethodService {

    public void methodSecured() {
        log.info("MethodService methodSecured()");
    }
}
