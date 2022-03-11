package studio.deervision.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SystemKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String systemKey;

    public SystemKeyAuthenticationToken(String systemKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.systemKey = systemKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return systemKey;
    }
}