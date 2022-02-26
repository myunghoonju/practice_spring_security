package practice.springsecurity.security.voter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import practice.springsecurity.security.service.SecurityResourceService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService service;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication auth,
                    Object obj, //filterInvocation
                    Collection<ConfigAttribute> attr) {//metadataSource
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        String remoteAddress = details.getRemoteAddress();
        List<String> accessIpList = service.getAccessIpList();

        for (String ip : accessIpList) {
            if (ip.equals(remoteAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        throw new AccessDeniedException("invalid ip");
    }
}
