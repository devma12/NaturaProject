package com.natura.web.server.configuration;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthenticationContextFactory.class)
public @interface WithAuthentication {

    boolean authentified() default true;

    String[] authorities() default {"basic user"};

    String username() default "testUser";
}
