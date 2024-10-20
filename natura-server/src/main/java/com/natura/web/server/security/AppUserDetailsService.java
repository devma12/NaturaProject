package com.natura.web.server.security;

import com.natura.web.server.entities.User;
import com.natura.web.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username);

    if (user != null) {
      AppUserDetails.AppUserBuilder builder = AppUserDetails.userBuilder();
      builder.username(user.getUsername());
      builder.email(user.getEmail());
      builder.password(user.getPassword());
      builder.roles(user.isFlowerValidator(), user.isInsectValidator());
      return builder.build();
    } else {
      throw new UsernameNotFoundException("User not found.");
    }
  }
}
