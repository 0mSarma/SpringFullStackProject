package com.shopom.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopom.admin.paging.SearchRepository;
import com.shopom.common.entity.User;


public interface UserRepository extends SearchRepository<User, Integer>{
	@Query("SELECT u FROM User u WHERE u.email = :email") // It is used to retrieve a User entity from a database using a custom SQL query.   
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")    //sql statement that assigns the given second value ?2 to enabled for the given intger value ?1 
	@Modifying   // this annotation indicates that this method modifies the database.
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	
}
 