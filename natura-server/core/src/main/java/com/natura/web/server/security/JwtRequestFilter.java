package com.natura.web.server.security;

import com.natura.web.server.model.User;
import com.natura.web.server.service.AuthenticationService;
import com.natura.web.server.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(final UserService userService,
                            final JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            boolean valid = jwtTokenUtil.validateToken(jwtToken) && validateUserToken(jwtToken);
            if (valid) {
                UserDetails userDetails = jwtTokenUtil.generateFromJWT(jwtToken);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebTokenAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        chain.doFilter(request, response);
    }

    public boolean validateUserToken(String jwtToken) {

        String username = jwtTokenUtil.getUsernameFromJWT(jwtToken);

        Optional<User> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            if (user.get().getToken() != null && user.get().getToken().equals(jwtToken)) {
                return true;
            } else {
                throw new JwtException("Invalid token for user.");
            }
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

}
