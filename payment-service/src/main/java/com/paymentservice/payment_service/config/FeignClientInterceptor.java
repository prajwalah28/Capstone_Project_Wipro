package com.paymentservice.payment_service.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();

            // Forward JWT or custom headers
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }

            // Forward role/username headers from gateway
            String role = request.getHeader("X-Role");
            String username = request.getHeader("X-Username");
            String userId = request.getHeader("X-UserId");

            if (role != null) template.header("X-Role", role);
            if (username != null) template.header("X-Username", username);
            if (userId != null) template.header("X-UserId", userId);
        }
    }
}
