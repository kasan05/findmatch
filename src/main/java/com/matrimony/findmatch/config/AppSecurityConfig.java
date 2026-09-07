package com.matrimony.findmatch.config;

import com.matrimony.findmatch.modal.UserStatus;
import com.matrimony.findmatch.service.AppUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity(debug = true)
@Configuration
public class AppSecurityConfig {

    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private AutoTokenFilter autoTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
         httpSecurity
                 .cors(Customizer.withDefaults())
                 .headers(header->
                         header.contentSecurityPolicy(csp->
                                 csp.policyDirectives("default-src 'self'; script-src 'self' http://localhost:8080/; object-src 'none';")))
                 .csrf(csrf->csrf.disable())
                 .authenticationProvider(authenticationProvider())
                 .sessionManagement(session->
                         session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(
                authorize->
                        authorize.requestMatchers("/broker/**","/auth/**","/user/**","/actuator/**").permitAll()
                                .requestMatchers("/friend/**").hasRole(UserStatus.APPROVED.name())
                                .requestMatchers("/ws/**","/user/history").hasRole(UserStatus.APPROVED.name())
                                .requestMatchers(HttpMethod.PUT,"/match-maker").hasRole(UserStatus.INCOMPLETE_DATA.name())
                                .requestMatchers("/match-maker/profile","/match-maker/profile/friend","/match-maker/data","/match-maker/profile/images"
                                ,"/match-maker/profile/data").hasRole(UserStatus.APPROVED.name())
                                .requestMatchers("/match-maker/personnel-data").hasAnyRole(UserStatus.INCOMPLETE_DATA.name(),
                                        UserStatus.APPROVED.name())
                                .anyRequest().authenticated()
        ).addFilterBefore(autoTokenFilter,UsernamePasswordAuthenticationFilter.class)
                 .httpBasic(HttpBasicConfigurer::disable);
        return   httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "X-Requested-With"

        ));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(appUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}