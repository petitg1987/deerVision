package studio.deervision.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RequestKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String requestKey;

    public RequestKeyAuthenticationToken(String requestKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.requestKey = requestKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return requestKey;
    }
}