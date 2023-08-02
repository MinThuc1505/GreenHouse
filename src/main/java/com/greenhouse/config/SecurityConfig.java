package com.greenhouse.config;


import java.util.Collection;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() throws Exception {

		UserBuilder users = User.builder();

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(users.username("user").password(getPasswordEncoder().encode("1")).roles("USER").build());

		manager.createUser(users.username("admin").password(getPasswordEncoder().encode("1")).roles("ADMIN").build());

		return manager;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.cors().disable().csrf().disable();
		
		http.securityMatcher("/admin/**","/client/order/**","/client/**","/rest/**","/client/signin","/client/login","/client/index","/client/error")
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/client/order/**")).hasRole("USER")
				.requestMatchers(
						new AntPathRequestMatcher("/client/**"),
						new AntPathRequestMatcher("/rest/**")
//						new AntPathRequestMatcher("/admin/**")
						).permitAll()
						.anyRequest().authenticated())
				.formLogin(login -> {
					try {
						login.loginPage("/client/signin")
								.loginProcessingUrl("/client/login")
								.defaultSuccessUrl("/client/index", false)
								.failureUrl("/client/error")
								.and()
								.exceptionHandling(exception -> exception.accessDeniedPage("/client/error"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}})
				.logout(logout -> logout
						.logoutUrl("/client/logout")
						.logoutSuccessUrl("/client/signin"));
		return http.build();
	}

//	@Bean
//	@Order(2)
//	public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(authorize -> authorize
//				.anyRequest()
//				.authenticated())
//				.formLogin(login -> {
//				try {
//					login.loginPage("/client/signin")
//							.loginProcessingUrl("/client/login")
//							.defaultSuccessUrl("/client/index", true)
//							.failureUrl("/client/error")
//							.and()
//							.exceptionHandling()
//					        .accessDeniedPage("/client/access-denied");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//		return http.build();
//	}
}
