package com.example.ssmstudy.config;


import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception{
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/api/articles/*/versions").authenticated();
            auth.requestMatchers(HttpMethod.GET, "/api/articles/*/versions/*").authenticated();
            auth.requestMatchers(HttpMethod.GET, "/api/articles/*/like").authenticated();
            auth.requestMatchers(HttpMethod.GET,
                    "/api/articles", "/api/articles/**").permitAll();
            auth.requestMatchers(HttpMethod.POST,
                    "/api/auth/register").permitAll();
            auth.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll();
            auth.requestMatchers(HttpMethod.GET,
                    "/api/auth/csrf").permitAll();
            auth.anyRequest().authenticated();
        });
        http.formLogin(form ->{
            form.permitAll().defaultSuccessUrl("/api/articles", true);
        });
        return http.build();
    }
}
