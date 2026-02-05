package com.OpenNika.AuthService.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.OpenNika.AuthService.Utility.JwtUtility;
import com.OpenNika.AuthService.Exception.ForbiddenException;
import com.OpenNika.AuthService.Exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.OpenNika.AuthService.Dto.Role;
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
            throw new UnauthorizedException("Missing Authorization header");
        }

        
        if (!jwtUtility.validateToken(authHeader)) {
            throw new UnauthorizedException("Invalid or expired JWT token");
        }

        String userId = jwtUtility.extractUserId(authHeader);
        Role role = jwtUtility.extractRole(authHeader);
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);

         String uri = request.getRequestURI();

        if (uri.startsWith("/api/auth/admin") && role != Role.ADMIN) {
            throw new ForbiddenException("Admin access required");
        }

        return true;
    }
}
