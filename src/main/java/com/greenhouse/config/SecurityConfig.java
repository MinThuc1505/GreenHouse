package com.greenhouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/**").permitAll()
				.anyRequest()
				.authenticated())
				.csrf().disable()
				.oauth2Login(oauth -> oauth.defaultSuccessUrl("/client/signin/compareEmail",true));
		return http.build();
	}
}
