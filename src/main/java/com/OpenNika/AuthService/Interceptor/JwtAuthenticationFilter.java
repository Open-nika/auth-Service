package com.OpenNika.AuthService.Interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import com.OpenNika.AuthService.Utility.JwtUtility;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;

    public JwtAuthenticationFilter(JwtUtility jwtUtility) {
        
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        String token = authHeader;
            
        System.out.println("Token received: " + token);
        
            if (jwtUtility.validateToken(token)) {
            
                String userId = jwtUtility.extractUserId(token);
                String role = jwtUtility.extractRole(token).name();
                System.out.println("Extracted role: " + role);
                System.out.println("Setting authority: ROLE_" + role);

                var authority = new SimpleGrantedAuthority("ROLE_" + role);
                System.out.println("Authority set: " + authority.getAuthority());
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singleton(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            

        filterChain.doFilter(request, response);
    }
}

