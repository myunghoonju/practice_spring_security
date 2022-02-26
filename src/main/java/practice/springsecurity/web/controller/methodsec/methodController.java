package practice.springsecurity.web.controller.methodsec;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import practice.springsecurity.domain.entity.AccountDto;
import practice.springsecurity.domain.service.MethodService;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class methodController {

    private final MethodService service;

    @PreAuthorize("hasRole('ROLE_USER') and #dto.username == principal.username")
    @GetMapping("/preauth")
    public String preAuth(AccountDto dto, Model model, Principal principal) {
        model.addAttribute("method", "@PreAuthorize ok");

        return "methodsec/method";
    }

    @GetMapping("/methodSecured")
    public String methodSecured(Model model) {
        service.methodSecured();
        model.addAttribute("method", "securedmethod");

        return "methodsec/method";
    }
}
