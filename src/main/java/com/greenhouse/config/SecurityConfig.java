package com.greenhouse.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
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
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Account account = accountDAO.findById(username).orElse(null);
                if (account == null) {
                    throw new UsernameNotFoundException("User not found");
                } 
                String userRole = account.getRole() ? "ADMIN" : "USER";
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
                return new User(account.getUsername(), account.getPassword(), Collections.singletonList(authority));
            }
        };
    }
	
	
	@Bean
	@Order(1)
	SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.cors().disable().csrf().disable();
		http.securityMatcher("/admin/**", "/client/order/**", "/client/**", "/rest/**", "/client/signin",
				"/client/login", "/client/index", "/client/error","/oauth2/authorization","/oauth2/authorization/google",
				"/login/oauth2/code/google","/oauth2/authorization/facebook","/login/oauth2/code/facebook")
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/client/order/**")).hasAnyRole("USER","ADMIN")
						.requestMatchers(
								new AntPathRequestMatcher("/client/**"),
								new AntPathRequestMatcher("/rest/**")
						).permitAll())
				.formLogin(login -> {
							try {
								login.loginPage("/client/signin")
								.loginProcessingUrl("/client/login")
								.defaultSuccessUrl("/client/signin/success", true)
								.failureUrl("/client/signin/error")
								.and()
								.exceptionHandling(exception -> exception.accessDeniedPage("/client/denied"));
							} catch (Exception e) {
								e.printStackTrace();
							}
						})
				.oauth2Login(oauth2Login ->
			        oauth2Login
			          .loginPage("/client/signin")
			          .defaultSuccessUrl("/client/social/success", true)
			          .failureUrl("/client/signin/error"))
				.logout(logout -> logout
						.logoutUrl("/client/logout")
						.logoutSuccessUrl("/client/logout/success"));
		return http.build();
	}
	
	
	@Bean
	ClientRegistrationRepository clientRegistrationRepository() {
	    List<ClientRegistration> registrations = new ArrayList();
	    registrations.add(this.googleClientRegistration());
	    registrations.add(this.facebookClientRegistration());
	    return new InMemoryClientRegistrationRepository(registrations);
	}

	private ClientRegistration googleClientRegistration() {
		return ClientRegistration.withRegistrationId("google")
			.clientId("609065578259-vdg4mj215nb1f2emqd5rid1m1tbojmm2.apps.googleusercontent.com")
			.clientSecret("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")
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.scope("openid", "profile", "email", "address", "phone")
			.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
			.tokenUri("https://www.googleapis.com/oauth2/v4/token")
			.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
			.userNameAttributeName(IdTokenClaimNames.SUB)
			.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
			.clientName("Google")
			.build();
	}
	
	private ClientRegistration facebookClientRegistration() {
	    return ClientRegistration.withRegistrationId("facebook")
	        .clientId("776354867561051")
	        .clientSecret("43b42a6f3c032746ce26915df52397d6")
	        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
	        .scope("email","public_profile")
	        .authorizationUri("https://www.facebook.com/v12.0/dialog/oauth")
	        .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
	        .userInfoUri("https://graph.facebook.com/v12.0/me?fields=id,name,email")
	        .userNameAttributeName("id")
	        .clientName("Facebook")
	        .build();
	}
}
