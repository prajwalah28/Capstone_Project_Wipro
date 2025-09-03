package com.ApiGateway.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ApiGateway.apigateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                // ✅ Auth service route - public (register, login)
                .route("auth-service-route", r -> r.path("/auth/**")
                        .uri("lb://AUTHSERVICE"))

                // ✅ Customer service route - secured by JWT filter
                .route("customer-service-route", r -> r.path("/api/customers/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            // No requiredRole here → CustomerController enforces role logic
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://CUSTOMERSERVICE"))

                // ✅ Account service route - secured by JWT filter
                .route("account-service-route", r -> r.path("/accounts/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://ACCOUNTSERVICE"))
                
                
                
                .route("transaction-service-route", r -> r.path("/transactions/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://TRANSACTION-SERVICE"))
                
                .route("payment-service-route", r -> r.path("/payments/**")
                        .filters(f -> {
                            JwtAuthenticationFilter.Config config = new JwtAuthenticationFilter.Config();
                            return f.filter(jwtAuthenticationFilter.apply(config));
                        })
                        .uri("lb://PAYMENT-SERVICE"))
                
                .build();
        
        
    }
}