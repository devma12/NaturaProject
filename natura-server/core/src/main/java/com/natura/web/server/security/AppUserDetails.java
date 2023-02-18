package com.natura.web.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AppUserDetails extends User {

    private String email;

    public AppUserDetails(String username, String email, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static AppUserBuilder userBuilder() {
        return new AppUserBuilder();
    }

    public static class AppUserBuilder {

        private String username;
        private String email;
        private String password;
        private List<GrantedAuthority> authorities;
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;

        private AppUserBuilder() {}

        public AppUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public AppUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public AppUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            String[] var3 = roles;
            int var4 = roles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String role = var3[var5];
                Assert.isTrue(!role.startsWith("ROLE_"), () ->
                    role + " cannot start with ROLE_ (it is automatically added)"
                );
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return this.authorities((Collection)authorities);
        }

        public AppUserBuilder roles(boolean isFlowerValidator, boolean isInsectValidator) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (isFlowerValidator)
                authorities.add(new SimpleGrantedAuthority("ROLE_FLOWER_VALIDATOR"));
            if (isInsectValidator)
                authorities.add(new SimpleGrantedAuthority("ROLE_INSECT_VALIDATOR"));

            return this.authorities(authorities);
        }

        public AppUserBuilder authorities(GrantedAuthority... authorities) {
            return this.authorities(Arrays.asList(authorities));
        }

        public AppUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public AppUserBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public AppUserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public AppUserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public AppUserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserDetails build() {
            if (this.authorities == null)
                this.authorities = new ArrayList<>();

            if (password == null)
                this.password("");

            return new AppUserDetails(this.username, this.email, this.password,
                    !this.disabled, !this.accountExpired, !this.credentialsExpired, !this.accountLocked,
                    this.authorities);
        }

    }
}
