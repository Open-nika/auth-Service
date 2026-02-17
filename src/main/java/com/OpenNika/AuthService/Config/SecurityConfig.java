package com.OpenNika.AuthService.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.OpenNika.AuthService.Interceptor.JwtAuthenticationFilter;
import com.OpenNika.AuthService.Utility.JwtUtility;

@Configuration
@EnableMethodSecurity

public class SecurityConfig {
     private final JwtUtility jwtUtility;

    public SecurityConfig(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
          http
          .csrf(csrf->csrf.disable())
          .sessionManagement(
            session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth->auth
                .requestMatchers("/api/auth/public/**").permitAll()
                .requestMatchers("/api/auth/admin/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()



            );

              http.addFilterBefore(
                jwtAuthenticationFilter(jwtUtility),
                UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtility jwtUtility) {
    return new JwtAuthenticationFilter(jwtUtility);
}

}
