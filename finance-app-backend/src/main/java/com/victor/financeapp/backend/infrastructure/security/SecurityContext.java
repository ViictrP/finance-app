package com.victor.financeapp.backend.infrastructure.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityContext {

    // Method to get the user ID reactively
    public static Mono<String> getUserId() {
        return getAuthentication().map(jwt -> jwt.getClaim("sub"));  // Return Mono of user ID
    }

    // Method to get the user email reactively
    public static Mono<String> getUserEmail() {
        return getAuthentication().map(jwt -> jwt.getClaim("email"));  // Return Mono of user email
    }

    // Method to get the Authentication reactively
    private static Mono<Jwt> getAuthentication() {
        // Use ReactiveSecurityContextHolder to get the current Authentication
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (Jwt) context.getAuthentication().getPrincipal());
    }
}
