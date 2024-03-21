package com.brunel.videolearning.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
//@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserAuthorityService userLoginService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Customize the authentication method by setting to use a custom class UserLoginService to implement this custom authentication method.
        // Additionally, specify using the Bcrypt algorithm as the encryption tool for passwords.
        auth.userDetailsService(userLoginService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Disable CSRF (Cross-Site Request Forgery) protection.
        http
                .csrf(csrf -> csrf.disable());

        //Allow cross-origin access to a specific frontend by instantiating the @Bean corsConfigurationSource for that frontend.
        http
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource())
                );
        //Set the session management to stateless. This means that access for a user is solely determined by whether the appropriate Authorization is included in the request headers,
        // and the server does not store any user login state.
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Execute the LoginFilter first, followed by the JWTFilter.
        http
                .addFilterBefore(new LoginFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(
                //The endpoint POST /login can be accessed without authorization.
                (authorize) -> authorize.requestMatchers(HttpMethod.POST, "/login").permitAll()
                        // all other requests require authentication before access.
                        .anyRequest().authenticated());
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //allows the frontend using interface at http://localhost:3000
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        //allows the http methods: GET or POST
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

