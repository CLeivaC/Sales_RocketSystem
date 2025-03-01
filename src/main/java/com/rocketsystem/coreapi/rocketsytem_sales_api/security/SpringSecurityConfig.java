package com.rocketsystem.coreapi.rocketsytem_sales_api.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.UserRepository;
import com.rocketsystem.coreapi.rocketsytem_sales_api.security.filter.JwtAuthenticationFilter;
import com.rocketsystem.coreapi.rocketsytem_sales_api.security.filter.JwtValidationFilter;


@Configuration
public class SpringSecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests( (authz) -> authz
        .requestMatchers(HttpMethod.GET,"/rocketsystem/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST,"/rocketsystem/users/register").permitAll()
        .requestMatchers(HttpMethod.PATCH,"/rocketsystem/users/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET,"/rocketsystem/products", "/rocketsystem/products/{id}").hasAnyRole("ADMIN","SELLER")
        .requestMatchers(HttpMethod.POST,"/rocketsystem/products").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT,"/rocketsystem/products/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT,"/rocketsystem/point-sales/{pointSaleId}/activate","/rocketsystem/point-sales/{pointSaleId}/desactivate").hasAnyRole("ADMIN","SELLER")
        .requestMatchers(HttpMethod.GET,"/rocketsystem/point-sales").hasAnyRole("ADMIN","SELLER")
        .requestMatchers(HttpMethod.POST,"/rocketsystem/point-sales").hasAnyRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/rocketsystem/auth/refresh").permitAll()
        // Permitir acceso sin autenticación a los endpoints de Swagger y OpenAPI
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() 
        .anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager(),userRepository))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config-> config.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(management-> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsfilter(){
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
