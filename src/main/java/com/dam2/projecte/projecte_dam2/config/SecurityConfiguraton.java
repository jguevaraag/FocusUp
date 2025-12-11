package com.dam2.projecte.projecte_dam2.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // Importación añadida
@Configuration
@EnableWebSecurity
public class SecurityConfiguraton {
    
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public SecurityConfiguraton(AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas
                .requestMatchers("/registro", "/registro/**", "/js/**", "/css/**", "/img/**", "/webjars/**" ).permitAll()
                .requestMatchers("/login", "/login/**").permitAll()
                
                // Nuevas rutas protegidas por rol:
                // Solo SuperAdmin puede acceder a su índice
                .requestMatchers("/index_super", "/index_super/**").hasRole("SUPERADMIN") 
                // Admin (y SuperAdmin, si tiene ese rol)
                .requestMatchers("/index_admin", "/index_admin/**").hasAnyRole("SUPERADMIN", "ADMIN")
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(successHandler)
                        .failureHandler(failureHandler))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}