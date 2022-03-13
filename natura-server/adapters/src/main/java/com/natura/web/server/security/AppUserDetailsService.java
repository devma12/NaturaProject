package com.natura.web.server.security;

import com.natura.web.server.ports.database.entities.UserEntity;
import com.natura.web.server.ports.database.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            AppUserDetails.AppUserBuilder builder = AppUserDetails.userBuilder();
            builder.username(user.get().getUsername());
            builder.email(user.get().getEmail());
            builder.password(user.get().getPassword());
            builder.roles(user.get().isFlowerValidator(), user.get().isInsectValidator());
            return builder.build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
