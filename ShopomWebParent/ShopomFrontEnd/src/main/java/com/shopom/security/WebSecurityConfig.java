 package com.shopom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopom.security.oauth.CustomerOAuth2UserService;
import com.shopom.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired private CustomerOAuth2UserService oAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler oAuth2loginHandler;
	@Autowired private DatabaseLoginSuccessHandler databaseLoginHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authorizeHttpRequests((authz) -> authz
    				.requestMatchers("/account_details","/update_account_detail", "/orders/**", "/cart", "/address_book/**", "/checkout", "/place_order")
    				.authenticated()
    				.anyRequest().permitAll())
    		.formLogin((formlogin) -> formlogin
    				.loginPage("/login")
    				.usernameParameter("email")
    				.successHandler(databaseLoginHandler)
    				.permitAll())
    		.oauth2Login((customer)-> customer 
    				.loginPage("/login")
    				.userInfoEndpoint((port)->port
    						.userService(oAuth2UserService))
    				.successHandler(oAuth2loginHandler))
    		.rememberMe((session) -> session
    				.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
    				.tokenValiditySeconds(14*24*69*60))
    		.sessionManagement((session) -> session
    				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
    		
        
        return http.build();   
    }

    @Bean							
    WebSecurityCustomizer customizer() {         //this allows file at the directory "/images/**", "/js/**", "/webjars/**" to bypass spring securities.
    	return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }


    @Bean
    UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	
    	authProvider.setUserDetailsService(userDetailsService());
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }

}




/*
 * .requestMatchers( "/users/**", "/categories/**", "/brands/**",
 * "/products/**", "/customers/**", "/shipping/**", "/orders/**", "/report/**",
 * "/articles/**", "/menus/**", "/settings/**").hasAuthority("Admin")
 * .requestMatchers("/products/**", "/customers/**", "/shipping/**",
 * "/orders/**", "/report/**").hasAuthority("Salesperson")
 * .requestMatchers("/categories/**", "/brands/**", "/products/**",
 * "/menus/**").hasAuthority("Editor") .requestMatchers("/products/**",
 * "/orders/**").hasAuthority("Shipper") .requestMatchers("/questions/**",
 * "/reviews/**").hasAuthority("Assistant")
 */
