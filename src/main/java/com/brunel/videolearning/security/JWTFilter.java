package com.brunel.videolearning.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;

/**
 * This JWTFiler is used in SecurityConfiguration after LoginFilter.
 */
public class JWTFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        Authentication authentication = null;

        HttpServletRequest req = (HttpServletRequest) request;

        String token = req.getHeader("Authorization");
        SecretKey key = Keys.hmacShaKeyFor(JWTProperties.signingkey.getBytes());
        Optional<String> opPrincipal;

        if (token != null) {
            try {
                opPrincipal = Optional.ofNullable(Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token.replace("Bearer ", ""))
                        .getPayload()
                        .getSubject());
                if (opPrincipal.isPresent()) {
                    authentication = new UsernamePasswordAuthenticationToken(opPrincipal.get(), null, new ArrayList<>());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }
}
