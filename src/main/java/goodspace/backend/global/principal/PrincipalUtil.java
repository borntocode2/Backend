package goodspace.backend.global.principal;

import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PrincipalUtil {
    public long findIdFromPrincipal(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
