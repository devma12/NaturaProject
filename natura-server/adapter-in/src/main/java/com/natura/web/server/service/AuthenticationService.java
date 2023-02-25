package com.natura.web.server.service;

import com.natura.web.server.exception.DataNotFoundException;
import com.natura.web.server.exception.ServerException;
import com.natura.web.server.exception.UserAccountException;
import com.natura.web.server.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    User register(String email, String username, String password) throws ServerException;

    User login(String email, String password) throws ServerException;

    User returnJwtWithUserDetailsFromAuthentication(Authentication auth)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException;

    User getAuthenticatedUserDetails(Authentication auth)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException;

    User logout(HttpServletRequest request, HttpServletResponse response)
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException;

    User authenticate()
            throws UserAccountException.AuthenticationException, UserAccountException.InvalidAccountDataException;

    User updateEmail(Long id, String email)
            throws DataNotFoundException, UserAccountException.DuplicateAccountException;

    User updatePassword(Long id, String oldPassword, String newPassword)
            throws DataNotFoundException, UserAccountException.AuthenticationException;
}
