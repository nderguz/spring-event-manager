package org.example.eventmanager.security;

import lombok.AllArgsConstructor;
import org.example.eventmanager.errorhandler.CustomAccessDeniedHandler;
import org.example.eventmanager.errorhandler.CustomAuthenticationEntryPoint;
import org.example.eventmanager.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Clock;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.debug(true).ignoring()
                .requestMatchers(
                  "/css/**",
                  "/js/**",
                  "/img/**",
                  "/lib/**",
                  "/favicon.ico",
                  "/swagger-ui/**",
                  "/v2/api-docs",
                  "/v3/api-docs",
                  "/configuration/ui",
                  "/swagger-resources/**",
                  "/configuration/security",
                  "/swagger-ui.html",
                  "/webjars/**",
                  "/v3/api-docs/swagger-config",
                  "/event-manager-openapi.yaml"
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(authorize ->{
                    authorize
                            .requestMatchers(HttpMethod.GET, "/locations").hasAnyAuthority("ADMIN", "USER")
                            .requestMatchers(HttpMethod.POST, "/locations").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/locations/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyAuthority("ADMIN", "USER")
                            .requestMatchers(HttpMethod.PUT, "/locations/{locationId}").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/users").permitAll()
                            .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                            .requestMatchers(HttpMethod.POST, "/events").hasAuthority("USER")
                            .requestMatchers(HttpMethod.GET, "/events/{eventId}").hasAnyAuthority("ADMIN", "USER")
                            .requestMatchers(HttpMethod.DELETE, "/events/{eventId}").hasAnyAuthority("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET, "/events/my").hasAuthority("USER")
                            .anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
