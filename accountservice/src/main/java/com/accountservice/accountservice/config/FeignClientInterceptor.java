package com.accountservice.accountservice.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

	@Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();

            String authHeader = request.getHeader("Authorization");
            String username = request.getHeader("X-Auth-Username");
            String role = request.getHeader("X-Auth-Role");

            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }
            if (username != null) {
                template.header("X-Auth-Username", username);
            }
            if (role != null) {
                template.header("X-Auth-Role", role);}
            }
        }
    }

