package id.ac.pnj.kanban.config;

import id.ac.pnj.kanban.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class KanbanSecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(bCryptPasswordEncoder());
        return auth;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer

                        .requestMatchers("/svg/**").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(form ->
                form
                        .loginPage("/show-login-page")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(authenticationSuccessHandler)
                        .permitAll()
        ).logout(logout ->
                logout.permitAll()
        )
                // .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
        ;
        return httpSecurity.build();
    }

}
