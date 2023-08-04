package com.greenhouse.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	
	@Autowired
	AccountDAO accountDAO;

	@Bean
	BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Account account = accountDAO.findById(username).orElse(null);

                if (account == null) {
                    throw new UsernameNotFoundException("User not found");
                }

                String userRole = account.getRole() ? "ADMIN" : "USER";
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
                return new User(account.getUsername(), getPasswordEncoder().encode(account.getPassword()), Collections.singletonList(authority));
            }
        };
    }
	
	
	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.cors().disable().csrf().disable();

		http.securityMatcher("/admin/**", "/client/order/**", "/client/**", "/rest/**", "/client/signin",
				"/client/login", "/client/index", "/client/error")
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/client/order/**")).hasAnyRole("USER","ADMIN")
						.requestMatchers(
								new AntPathRequestMatcher("/client/**"),
								new AntPathRequestMatcher("/rest/**")
						).permitAll().anyRequest().authenticated())
				.formLogin(login -> {
							try {
								login.loginPage("/client/signin")
								.loginProcessingUrl("/client/login")
								.defaultSuccessUrl("/client/signin/success", true)
								.failureUrl("/client/signin/error")
								.and()
								.exceptionHandling(exception -> exception.accessDeniedPage("/client/denied"));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						})
				.logout(logout -> logout
						.logoutUrl("/client/logout")
						.logoutSuccessUrl("/client/logout/success"));
		return http.build();
	}
}
