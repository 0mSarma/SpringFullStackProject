package com.shopom.admin.security;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.shopom.common.entity.Role;
import com.shopom.common.entity.User;

public class ShopomUserDetails implements UserDetails {          // this class is commonly used in the Spring Security framework to represent user details for authentication and authorization purposes.
	
	private static final long serialVersionUID = 1L;
	private User user;
	
	

	public ShopomUserDetails(User user) {       //VIC
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {  //this method is used by the spring securites to get the list of assigned roles of the user
		Set<Role> roles = user.getRoles(); // creates a set if roles from the user entity as the user dont have same role twice
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();   // creates an empty Arraylist of objects of the class SimpleGrantedAuthority
		for(Role role : roles) { //loops through the elements of the roles set and adds each role to the authorities list
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities; //returns the authorities list that contains roles of the assinged user.
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public String getFullname() {
		return this.user.getFirstName() + " " + this.user.getLastName();
	}

	public void setFirstName(String firstName) {
		this.user.setLastName(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	
	
}
