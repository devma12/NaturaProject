package com.natura.web.server.services;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.InvalidDataException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.security.JwtTokenUtil;
import com.natura.web.server.security.WebTokenAuthenticationDetails;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public User register(String email, String username, String password) throws ServerException {

        if (email == null)
            throw new UserAccountException.MandatoryUserDetailException("Email");
        if (username == null)
            throw new UserAccountException.MandatoryUserDetailException("Username");
        if (password == null)
            throw new UserAccountException.MandatoryUserDetailException("Password");

        // Verify there is no existing account with same email address
        User found = userRepository.findByEmail(email);
        if (found != null) {
            throw new UserAccountException.DuplicateAccountException("email: " + email);
        }

        // Verify there is no existing account with same username
        found = userRepository.findByUsername(username);
        if (found != null) {
            throw new UserAccountException.DuplicateAccountException("username: " + username);
        }

        // Create new user with given details
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        // save user in db
        user = userRepository.save(user);

        // authenticate
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        user = returnJwtWithUserDetailsFromAuthentication(auth);

        return user;
    }

    public User login(String email, String password) throws ServerException {

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return returnJwtWithUserDetailsFromAuthentication(auth);
        } catch (DisabledException e) {
            throw new InvalidDataException("username");
        } catch (BadCredentialsException e) {
            throw new InvalidDataException("credentials");
        }

    }

    public User returnJwtWithUserDetailsFromAuthentication(Authentication auth) {
        // Get user
        User user = getUserFromAuthentication(auth, true);

        // Generate token
        final String token = jwtTokenUtil.generateToken(auth);
        user.setToken(token);

        // update user with new token in db
        user = userRepository.save(user);

        // remove password to sent user as response
        user.setPassword(null);
        return user;
    }

    public User getAuthenticatedUserDetails(Authentication auth) {
        User user = getUserFromAuthentication(auth, false);

        String token = ((WebTokenAuthenticationDetails)auth.getDetails()).getTokenValue();
        user.setToken(token);

        return user;
    }

    private User getUserFromAuthentication(Authentication auth, boolean withPassword) {
        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            User user = new User(details.getUsername());
            this.getInternalUserData(user, withPassword);
            return user;
        } else {
            throw new InsufficientAuthenticationException("invalid authentication");
        }
    }

    private void getInternalUserData(User user, boolean withPassword) {
        User storedUser = userRepository.findByUsername(user.getUsername());
        if (storedUser != null) {
            user.setId(storedUser.getId());
            user.setEmail(storedUser.getEmail());
            if (withPassword)
                user.setPassword(storedUser.getPassword());
        }
    }

    public boolean validateUserToken(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJWT(jwtToken);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (user.getToken() != null && user.getToken().equals(jwtToken)) {
                return true;
            } else {
                throw new JwtException("Invalid token for user.");
            }
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User logged = getUserFromAuthentication(auth, true);

        // default spring logout
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // reset token in db
        logged.setToken(null);
        userRepository.save(logged);

        return true;
    }

    public User authenticate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getAuthenticatedUserDetails(auth);
    }
}
