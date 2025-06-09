package br.com.comigo.autenticador.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.comigo.autenticador.application.service.UserDetailsServiceImpl;
import br.com.comigo.autenticador.infrastructure.security.AuthenticationEntryPointJwt;
import br.com.comigo.autenticador.infrastructure.security.AuthorizationTokenFilter;
import br.com.comigo.autenticador.infrastructure.security.RestAccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationEntryPointJwt authenticationEntryPointJwt;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    AuthorizationTokenFilter jwtTokenFilter() {
        return new AuthorizationTokenFilter();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPointJwt)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/sign-in**").permitAll()
                        .anyRequest().authenticated());

        http.authenticationProvider(this.authenticationProvider());

        http.addFilterBefore(this.jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}