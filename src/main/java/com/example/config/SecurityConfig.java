package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        // Enable CORS with the custom configuration
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        
	        // Disable CSRF for stateless JWT-based APIs
	        .csrf(csrf -> csrf.disable())

	        // Authorization rules for public and secure routes
	        .authorizeHttpRequests(authz -> authz
	            .requestMatchers("/index.html", "/", "/auth/**", "/register").permitAll()  // Public endpoints like login
	            .requestMatchers("/user").authenticated()  // Secure /user endpoint
	            .anyRequest().authenticated()  // Secure all other endpoints
	        )
	        
	        // Stateless session management for APIs
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    
	        // Add the JWT authentication filter before the default username/password filter
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}




	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.addAllowedOrigin("http://localhost:4200");  // Allow requests from Angular frontend
	    corsConfig.addAllowedMethod(CorsConfiguration.ALL);  // Allow all HTTP methods (GET, POST, PUT, DELETE)
	    corsConfig.addAllowedHeader(CorsConfiguration.ALL);  // Allow all headers
	    corsConfig.addExposedHeader("Authorization");  // Expose the Authorization header to the frontend
	    corsConfig.setAllowCredentials(true);  // Allow credentials (cookies, authorization headers, etc.)

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);

	    return source;
	}


	     @Bean
	     AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
	         AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
	         authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	         return authenticationManagerBuilder.build();
	     }

	     @Bean
	     PasswordEncoder passwordEncoder() {
	         return new BCryptPasswordEncoder();  // Use BCrypt for password encoding
	     }
	 }
