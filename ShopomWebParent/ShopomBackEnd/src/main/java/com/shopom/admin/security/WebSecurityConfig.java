package com.shopom.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	UserDetailsService userDetailsService() {    // this returns an instance of shopomUserDetailsService
		return new ShopomUserDetailsService();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    public DaoAuthenticationProvider authenticationProvider() {   									//   This provider is used for authenticating users by looking up their details from a data access object (DAO),                   /
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();		 		   //    typically a UserDetailsService, and then performing password validation. 
    	authProvider.setUserDetailsService(userDetailsService());								  //
    	authProvider.setPasswordEncoder(passwordEncoder());										 //      Its primary job is to validate user credentials (such as username and password) and perform user authentication using a user details service and password encoder.
    	return authProvider;
   
    }
		
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authorizeHttpRequests((authz) -> authz
    				.requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
    				.requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
    				.requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
    				.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
    				.requestMatchers("/products/edit/**", "/products/save/**", "/products/check_unique/**").hasAnyAuthority("Admin", "Editor", "Salesperson")
    				.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
    				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor", "Salesperson")
    				.requestMatchers("/orders", "/orders/", "/orders/page/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
    				.requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")
    				.requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
    				.anyRequest().authenticated())
    		.formLogin((formLogin) -> formLogin            //  this redirects everyone towards the
    				.loginPage("/login")				   //   custom login page /login.
    				.usernameParameter("email")				//
    				.permitAll())                           //	the controller for this page is the main controller
    		.logout((logout)-> logout
    				.permitAll())
    		.rememberMe((session) -> session               // for enabling rememmber me during login
    				.key("AbCdEfGhIjKlMnOpQrStUvWxYz_1234567890")
    				.tokenValiditySeconds(7*24*60*60));  //leds users to the admin login page to authenticate 
    	
    	http.headers((config)-> config
    			.frameOptions((option)-> option
    					.sameOrigin()));
    	 
        http.authenticationProvider(authenticationProvider());   // sets the user authencatication provider
        
        
        return http.build();   
    }

    @Bean							
    WebSecurityCustomizer customizer() {         //this allows file at the directory "/images/**", "/js/**", "/webjars/**" to bypass spring securities.
    	return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
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
