package com.shopom.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopom.admin.paging.PagingAndSortingHelper;
import com.shopom.common.entity.Role;
import com.shopom.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional    //if any exception occures during ithe class method execution. the changes are rolled back, ensuring that no changes are persisted to the database.
public class UserService {
	public static final Integer USER_PER_PAGE = 4; 

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;          // injects passwordEncoder bean to this service class
	
	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	public List<User> listAllUser(){
		List<User> allusers = (List<User>) userRepo.findAll();
		return allusers;	
	}

	public List<Role> listAllRole(){
		return (List<Role>) roleRepo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, USER_PER_PAGE, userRepo);
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);   //to check if its a new user or old user being updated
		
		if(isUpdatingUser) {  
			User existingUser = userRepo.findById(user.getId()).get();   // if its an existing user then its assigned to the existingUser variable
			
			if(user.getPassword().isEmpty()) {       // if the new user object password is empty that means the user does not want to change password
				user.setPassword(existingUser.getPassword());     // so we assign the existing user password which is already encoded to the new user object using a setter method
			}else {
				encodePassword(user);               // else we encode the new password provided by the user using the password encoded method
			}
		} else {
			encodePassword(user);   // just encode the new user password
		}
		
		return userRepo.save(user);	
	}
	
	
	
	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();
		
		if (!userInForm.getPassword().isEmpty()) {              // here we check weather the user provide new password in account form
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		return userRepo.save(userInDB);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());      //when a user object is passed through here it takes its original passeord encodes it
		user.setPassword(encodedPassword);										 //  sets the new passwords as the encoded password 
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}



	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();    //this method can throw a no such element exception.
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID" + id);     // this optional exception class is created named UserNotFoundException which returns an error message.
		}	
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);    //asigns how many items are there in the database that has the given id
		if(countById == null || countById == 0) {    // checks if the value is null or zero
			throw new UserNotFoundException("Could not find any user with ID" + id);    // if yes then then UserNotFoundException is thrown
		}
		userRepo.deleteById(id);      //otherwise the user is deleted by id
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}
	
	
	
}
