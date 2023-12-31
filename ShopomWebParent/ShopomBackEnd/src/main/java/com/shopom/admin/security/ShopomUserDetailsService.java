package com.shopom.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopom.admin.user.UserRepository;
import com.shopom.common.entity.User;

public class ShopomUserDetailsService implements UserDetailsService {   //this class is used by spring security to perform user authentication

	@Autowired
	private UserRepository userRepo;     //we need to have a refrence to the user repository interface
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email); //with the given email by the user it serches the user repository for the matching email
		if (user != null) {							//and assign it to a user object if found
			return new ShopomUserDetails(user);		// and the found user is used to create an instance of the ShopOmUserDetails class.
		} 
		throw new UsernameNotFoundException(" Could not find user with email ");
		
	
	}

}
