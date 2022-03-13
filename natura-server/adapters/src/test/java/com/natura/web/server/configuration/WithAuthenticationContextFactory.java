package com.natura.web.server.configuration;

import com.natura.web.server.security.AppUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WithAuthenticationContextFactory implements WithSecurityContextFactory<WithAuthentication> {
    @Override
    public SecurityContext createSecurityContext(WithAuthentication withAuthentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if (!withAuthentication.authentified()) {
            context.setAuthentication(null);
        } else {
            UserDetails details = AppUserDetails.userBuilder()
                    .username(withAuthentication.username())
                    .email("mail")
                    .authorities(Arrays.stream(withAuthentication.authorities())
                            .map(authority -> new SimpleGrantedAuthority(authority))
                            .collect(Collectors.toList()))
                    .build();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
            context.setAuthentication(authentication);
        }
        return context;
    }
}
