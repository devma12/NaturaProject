package com.natura.web.server.unit.services;

import com.natura.web.server.entities.User;
import com.natura.web.server.exceptions.ServerException;
import com.natura.web.server.exceptions.UserAccountException;
import com.natura.web.server.repository.UserRepository;
import com.natura.web.server.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void init() {

        Mockito.lenient().when(userRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    }

    @Test
    void registerNewValidUser() throws ServerException {
        String username = "testUser";
        String password = "pwd";

        User user = new User();
        user.setId((long) 1);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        Mockito.lenient().when(userRepository.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.lenient().when(userRepository.findByUsername(Mockito.any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count == 0) {
                    count++;
                    return null;
                }

                return user;
            }
        });

        User newUser = userService.register("test@exemple.com", username, password);
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
