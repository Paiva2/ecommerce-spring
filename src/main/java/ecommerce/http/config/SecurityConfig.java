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
                return http.csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/v1/clients/profile")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/category/list")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/product/{productId}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/category/*")
                                                .hasRole("ADMIN").anyRequest().permitAll())
                                .addFilterBefore(securityFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
