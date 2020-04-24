package com.natura.web.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.token.expiration.ms}")
    private int expiration;
    
	protected final Log logger = LogFactory.getLog(getClass());
    
    public String generateToken(Authentication authentication) {
        if (authentication == null)
            throw new InsufficientAuthenticationException("Null authentication.");

        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof AppUserDetails) {
            AppUserDetails userPrincipal = (AppUserDetails) authentication.getPrincipal();

            Claims claims = generateClaimsForUser(userPrincipal);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } else {
            throw new InsufficientAuthenticationException("Principal does not instance of AppUserDetails.");
        }
    }
    
    private Claims generateClaimsForUser(AppUserDetails user) {
    	
    	Map<String, Object> info =  new HashMap<String, Object>();
    	info.put("mail", user.getEmail());
    	
    	Claims claims = Jwts.claims().setSubject(user.getUsername());
    	claims.putAll(info);

        return claims;
    }
    
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
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
        builder.email((String) claims.get("mail"));
        UserDetails details = builder.build();
        return details;
	}
}