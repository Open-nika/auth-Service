package com.OpenNika.AuthService.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.OpenNika.AuthService.Utility.JwtUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtility jwtUtility;
    public JwtInterceptor(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
                                
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        
        if (!jwtUtility.validateToken(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String userId = jwtUtility.extractUserId(authHeader);
        request.setAttribute("userId", userId);

        return true;
    }
}
