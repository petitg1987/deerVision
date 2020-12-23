package com.urchin.release.mgt.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String userKey;

    public UserKeyAuthenticationToken(String userKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userKey = userKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userKey;
    }
}