package com.natura.web.server.security;

import com.natura.web.server.exception.ServerException;
import com.natura.web.server.exception.UserAccountException;
import com.natura.web.server.model.User;
import com.natura.web.server.provider.UserProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class NaturaAuthenticationServiceTest {

    @Autowired
    NaturaAuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    UserProvider userProvider;

    @BeforeEach
    void init() {

        Mockito.lenient().when(userProvider.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    }

    @Test
    void registerNewValidUser() throws ServerException {
        String username = "testUser";
        String password = "pwd";

        User user = new User();
        user.setId((long) 1);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        Mockito.lenient().when(userProvider.getUserByEmail(Mockito.any())).thenReturn(null);
        Mockito.lenient().when(userProvider.getUserByUsername(Mockito.any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count == 0) {
                    count++;
                    return null;
                }

                return user;
            }
        });

        User newUser = authenticationService.register("test@exemple.com", username, password);
        Assertions.assertNotNull(newUser);
    }

    @Test
    void registerUserWithoutEmail() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            authenticationService.register(null, "testUser", "pwd");
        });
    }

    @Test
    void registerUserWithoutUsername() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            authenticationService.register("test@exemple.com", null, "pwd");
        });
    }

    @Test
    void registerUserWithoutPassword() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            authenticationService.register("test@exemple.com", "testUser", null);
        });
    }

    @Test
    void registerUserWithUsedEmail() throws ServerException {
        Mockito.lenient().when(userProvider.getUserByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.lenient().when(userProvider.getUserByUsername(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(UserAccountException.DuplicateAccountException.class, () -> {
            authenticationService.register("test@exemple.com", "testUser", "pwd");
        });
    }

    @Test
    void registerUserWithUsedUsername() throws ServerException {
        Mockito.lenient().when(userProvider.getUserByEmail(Mockito.any())).thenReturn(null);
        Mockito.lenient().when(userProvider.getUserByUsername(Mockito.any())).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserAccountException.DuplicateAccountException.class, () -> {
            authenticationService.register("test@exemple.com", "testUser", "pwd");
        });
    }
}
