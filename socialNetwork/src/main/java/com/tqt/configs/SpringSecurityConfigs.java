/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqt.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author Quang Truong
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
                "com.tqt.controllers",
                "com.tqt.repositories",
                "com.tqt.services",
                "com.tqt.components"
})
public class SpringSecurityConfigs {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
                return new HandlerMappingIntrospector();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(c -> c.disable())
                                .authorizeHttpRequests(requests -> requests.requestMatchers("/", "/home")
                                                .authenticated()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/api/login", "/api/register").permitAll()
                                                .requestMatchers("/api/secure/**").authenticated()
                                                .requestMatchers("/api/**").permitAll()
                                                .requestMatchers("/ws/**").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(new com.tqt.filters.JwtFilter(),
                                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                                .formLogin(form -> form.loginPage("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/admin/", true)
                                                .failureUrl("/login?error=true").permitAll())
                                .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
                                .exceptionHandling(ex -> ex
                                                .defaultAuthenticationEntryPointFor(
                                                                (request, response, authException) -> response
                                                                                .sendError(401, "Unauthorized"),
                                                                new org.springframework.security.web.util.matcher.AntPathRequestMatcher(
                                                                                "/api/**"))
                                                .accessDeniedPage("/login?denied=true"));
                return http.build();
        }

        @Bean
        public Cloudinary cloudinary() {
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                                "cloud_name", "dabb0yavq",
                                "api_key", "629417998313995",
                                "api_secret", "Pz7QaOBFl3nGzZVfWWmb2Vvx3DQ",
                                "secure", true));
                return cloudinary;
        }

        @Bean
        @Order(0)
        public StandardServletMultipartResolver multipartResolver() {
                return new StandardServletMultipartResolver();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(List.of(
                                "http://localhost:3000/",
                                "https://social-network-frontend-omega.vercel.app"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);

                return source;
        }
}
