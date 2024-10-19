package com.natura.web.server.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtil {

    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_MAIL = "mail";
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.expiration.ms}")
    private int expiration;

    protected final Log logger = LogFactory.getLog(getClass());

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        if (authentication == null)
            throw new InsufficientAuthenticationException("Null authentication.");

        if (authentication.getPrincipal() instanceof AppUserDetails userPrincipal) {
            Claims claims = generateClaimsForUser(userPrincipal);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
        } else {
            throw new InsufficientAuthenticationException("Principal does not instance of AppUserDetails.");
        }
    }

    private Claims generateClaimsForUser(AppUserDetails user) {
        Map<String, Object> info = new HashMap<>();
        info.put(CLAIM_MAIL, user.getEmail());
        info.put(CLAIM_AUTHORITIES, user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList());
        return Jwts.claims().subject(user.getUsername()).add(info).build();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String getUsernameFromJWT(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public UserDetails generateFromJWT(String jwtToken) {
        String username = getUsernameFromJWT(jwtToken);
        Claims claims = getAllClaimsFromToken(jwtToken);
        AppUserDetails.AppUserBuilder builder = AppUserDetails.userBuilder();
        builder.username(username);
        builder.email((String) claims.get(CLAIM_MAIL));
        if (claims.get(CLAIM_AUTHORITIES) != null) {
            List<GrantedAuthority> authorities = claims.get(CLAIM_AUTHORITIES, List.class)
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                .toList();
            builder.authorities(authorities);
        }
        return builder.build();
    }
}