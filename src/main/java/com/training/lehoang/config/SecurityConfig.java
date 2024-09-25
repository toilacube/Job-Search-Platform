package com.training.lehoang.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.training.lehoang.modules.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;

    private final CustomUserDetailsService customUserDetailsService;

    private static final String[] ADMIN_MATCHERS ={
            "/job/**", "/test/**"
    };

    private static final String[] PUBLIC_MATCHERS = {
            // /error because So. whenever an exception is thrown and it is not handled by
            // any @ExceptionHandler annotated method, then Spring boot will call this endpoint (/error)
            "/error/**",
            "/auth/**",
            "/user/**",
            "/job/search/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                                .requestMatchers(ADMIN_MATCHERS).hasRole("ADMIN")
                                .anyRequest().authenticated()

                                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .userDetailsService(customUserDetailsService)
                ;

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");
        return NimbusJwtDecoder
                .withSecretKey(keySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
