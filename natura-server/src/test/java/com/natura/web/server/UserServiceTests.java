package com.natura.web.server;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.MandatoryDataAccountException;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.repo.UserRepository;
import com.natura.web.server.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        Mockito.lenient().when(userRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    }

    @Test
    void registerNewValidUser() throws ServerException {
        User newUser = userService.register("test@exemple.com", "testUser", "pwd");
        Assertions.assertNotNull(newUser);
    }

    @Test
    void registerUserWithoutEmail() throws ServerException {
        Assertions.assertThrows(MandatoryDataAccountException.class, () -> {
            User newUser = userService.register(null, "testUser", "pwd");
        });
    }

    @Test
    void registerUserWithoutUsername() throws ServerException {
        Assertions.assertThrows(MandatoryDataAccountException.class, () -> {
            User newUser = userService.register("test@exemple.com", null, "pwd");
        });
    }

    @Test
    void registerUserWithoutPassword() throws ServerException {
        Assertions.assertThrows(MandatoryDataAccountException.class, () -> {
            User newUser = userService.register("test@exemple.com", "testUser", null);
        });
    }
}
