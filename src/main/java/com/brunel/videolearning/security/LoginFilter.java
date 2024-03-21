package com.brunel.videolearning.security;

import java.io.IOException;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;

/**
 * This LoginFilter is used in SecurityConfiguration before JWTFilter.
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(AuthenticationManager authManager) {
        //  login endpoint only support HTTP Post method
        super(new AntPathRequestMatcher("/login","POST"));
        this.setAuthenticationManager(authManager);
    }

    //This method is called when a user requests to login.
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {

        //Extract the email and password entered by the user from the request HttpServletRequest req,
        // and place them into the creds object following the parsing method specified by the class CredentialsForLogin.
        CredentialsForLogin creds = new ObjectMapper()
                .readValue(req.getInputStream(), CredentialsForLogin.class);
        //Generate a Token under the Spring Security framework using the combination of the email and password as parameters.
        return this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getEmail(),
                        creds.getPassword()
                )
        );
    }

    //This method is called by the Spring Security framework when it deems the user is authorized to login,
    // to generate a token for the user to use after logging in, which is then returned to the user in the HTTP response headers.
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {

        SecretKey key = Keys.hmacShaKeyFor(JWTProperties.signingkey.getBytes());
        String JwtToken = Jwts.builder().subject(auth.getName()).expiration(new Date(System.currentTimeMillis() + JWTProperties.expirationtime)).signWith(key).compact();
        System.out.println(JwtToken);
        res.addHeader("Authorization", "Bearer " + JwtToken);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");

    }
}