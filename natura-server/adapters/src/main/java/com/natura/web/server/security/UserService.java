package com.natura.web.server.security;

import com.natura.web.server.exceptions.DataNotFoundException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.model.User;
import com.natura.web.server.providers.UserProvider;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserProvider userProvider;

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
        Optional<User> found = userProvider.getUserByEmail(email);
        if (found.isPresent()) {
            throw new UserAccountException.DuplicateAccountException("email: " + email);
        }

        // Verify there is no existing account with same username
        found = userProvider.getUserByUsername(username);
        if (found.isPresent()) {
            throw new UserAccountException.DuplicateAccountException("username: " + username);
        }

        // Create new user with given details
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        // save user in db
        user = userProvider.save(user);

        // authenticate
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // get user with new generated token
        user = returnJwtWithUserDetailsFromAuthentication(auth);

        return user;
    }

    public User login(String email, String password) throws ServerException {

        try {
            // authenticate
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // get user with new generated token
            return returnJwtWithUserDetailsFromAuthentication(auth);
        } catch (DisabledException e) {
            throw new UserAccountException.InvalidAccountDataException("username");
        } catch (BadCredentialsException e) {
            throw new UserAccountException.InvalidAccountDataException("credentials");
        }
    }

    public User returnJwtWithUserDetailsFromAuthentication(Authentication auth)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {
        // Get user
        User user = getUserFromAuthentication(auth, true);

        // Generate token
        final String token = jwtTokenUtil.generateToken(auth);
        user.setToken(token);

        // update user with new token in db
        user = userProvider.save(user);

        // remove password to sent user as response
        user.setPassword(null);
        return user;
    }

    public User getAuthenticatedUserDetails(Authentication auth)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {

        User user = getUserFromAuthentication(auth, false);

        String token = ((WebTokenAuthenticationDetails) auth.getDetails()).getTokenValue();
        user.setToken(token);

        return user;
    }

    private User getUserFromAuthentication(Authentication auth, boolean withPassword)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {

        if (auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            User user = new User(details.getUsername());
            this.getInternalUserData(user, withPassword);
            return user;
        } else {
            throw new UserAccountException.AuthenticationException("Invalid authentication");
        }
    }

    private void getInternalUserData(User user, boolean withPassword)
            throws UserAccountException.InvalidAccountDataException {

        Optional<User> storedUser = userProvider.getUserByUsername(user.getUsername());
        if (storedUser.isPresent()) {
            user.setId(storedUser.get().getId());
            user.setEmail(storedUser.get().getEmail());
            if (withPassword)
                user.setPassword(storedUser.get().getPassword());

            user.setFlowerValidator(storedUser.get().isFlowerValidator());
            user.setInsectValidator(storedUser.get().isInsectValidator());
        } else {
            throw new UserAccountException.InvalidAccountDataException("user with username " + user.getUsername());
        }
    }

    public boolean validateUserToken(String jwtToken) {

        String username = jwtTokenUtil.getUsernameFromJWT(jwtToken);

        Optional<User> user = userProvider.getUserByUsername(username);

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

    public User logout(HttpServletRequest request, HttpServletResponse response)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User logged = getUserFromAuthentication(auth, true);

        // default spring logout call
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // reset token in db
        logged.setToken(null);
        return userProvider.save(logged);
    }

    public User authenticate()
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getAuthenticatedUserDetails(auth);
    }

    public User updateEmail(Long id, String email)
            throws DataNotFoundException, UserAccountException.DuplicateAccountException {

        User user = userProvider.getUserById(id).orElse(null);
        if (user == null)
            throw new DataNotFoundException("User not found.");

        // Check email is not already used by another user
        Optional<User> duplicated = userProvider.getUserByEmail(email);
        if (duplicated.isPresent() && duplicated.get() != user)
            throw new UserAccountException.DuplicateAccountException("email: " + email);

        // update email and save user
        user.setEmail(email);
        return userProvider.save(user);
    }

    public User updatePassword(Long id, String oldPassword, String newPassword)
            throws DataNotFoundException, UserAccountException.AuthenticationException {

        User user = userProvider.getUserById(id).orElse(null);
        if (user == null)
            throw new DataNotFoundException("User not found.");

        // Check old password is correct
        boolean authenticate = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());

        if (authenticate) {
            // update password and save user
            String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            return userProvider.save(user);
        } else {
            throw new UserAccountException.AuthenticationException("Old password is invalid.");
        }
    }
}