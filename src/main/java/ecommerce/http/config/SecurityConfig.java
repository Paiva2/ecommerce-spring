package ecommerce.http.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        SecurityFilter securityFilter;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                // Admin only
                String[] forbiddenPosts = {"/api/v1/category/*", "/api/v1/sku/*", "/api/v1/sku/**",
                                "/api/v1/product/*"};

                String[] forbiddenPatchs = {"/api/v1/product/*", "/api/v1/sku/**",
                                "/api/v1/product/*", "/api/v1/category/**", "/api/v1/order/**"};

                String[] forbiddenDeletes =
                                {"/api/v1/sku/**", "api/v1/product/**", "/api/v1/category/**"};

                String[] forbiddenGets = {"/api/v1/order/*"};

                // Authenticated only
                String[] authGets = {"/api/v1/clients/profile", "api/v1/clients/orders"};

                return http.csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(HttpMethod.GET, authGets)
                                                .authenticated()
                                                .requestMatchers(HttpMethod.GET, forbiddenGets)
                                                .authenticated()
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/v1/order/new")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.PATCH, forbiddenPatchs)
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE,
                                                                forbiddenDeletes)
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, forbiddenPosts)
                                                .hasRole("ADMIN").anyRequest().permitAll())

                                .addFilterBefore(securityFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
