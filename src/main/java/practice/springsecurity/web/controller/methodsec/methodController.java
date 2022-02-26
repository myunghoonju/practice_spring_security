package practice.springsecurity.web.controller.methodsec;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import practice.springsecurity.domain.entity.AccountDto;

import java.security.Principal;

@Controller
public class methodController {

    @PreAuthorize("hasRole('ROLE_USER') and #dto.username == principal.username")
    @GetMapping("/preauth")
    public String preAuth(AccountDto dto, Model model, Principal principal) {
        model.addAttribute("method", "@PreAuthorize ok");

        return "methodsec/method";
    }
}
