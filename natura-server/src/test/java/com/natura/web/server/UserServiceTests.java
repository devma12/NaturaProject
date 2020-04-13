package com.natura.web.server;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void init() {
        Mockito.lenient().when(userRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    }

    @Test
    void registerNewValidUser() throws ServerException {
        Mockito.lenient().when(userRepository.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.lenient().when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        User newUser = userService.register("test@exemple.com", "testUser", "pwd");
        Assertions.assertNotNull(newUser);
    }

    @Test
    void registerUserWithoutEmail() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            userService.register(null, "testUser", "pwd");
        });
    }

    @Test
    void registerUserWithoutUsername() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            userService.register("test@exemple.com", null, "pwd");
        });
    }

    @Test
    void registerUserWithoutPassword() throws ServerException {
        Assertions.assertThrows(UserAccountException.MandatoryUserDetailException.class, () -> {
            userService.register("test@exemple.com", "testUser", null);
        });
    }

    @Test
    void registerUserWithUsedEmail() throws ServerException {
        Mockito.lenient().when(userRepository.findByEmail(Mockito.any())).thenReturn(new User());
        Mockito.lenient().when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(UserAccountException.DuplicateAccountException.class, () -> {
            userService.register("test@exemple.com", "testUser", "pwd");
        });
    }

    @Test
    void registerUserWithUsedUsername() throws ServerException {
        Mockito.lenient().when(userRepository.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.lenient().when(userRepository.findByUsername(Mockito.any())).thenReturn(new User());

        Assertions.assertThrows(UserAccountException.DuplicateAccountException.class, () -> {
            userService.register("test@exemple.com", "testUser", "pwd");
        });
    }
}
