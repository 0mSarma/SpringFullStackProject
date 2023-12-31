package com.shopom.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopom.common.entity.Role;
import com.shopom.common.entity.User;

@DataJpaTest  //indicates this as a test class
@AutoConfigureTestDatabase(replace = Replace.NONE)	 //annotation to run the test against the real database not in memory database 
@Rollback(false)  //not reverting any changes made to the database during the test.
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;  // this class is provided by spring data JPA for Unit testing with repository 							______________________
											  //          in this case because the user table has a relation with roles table so we create an instance of                    /
											//              the role class for testing purpose.																				/	
																								//																		   /															
	@Test																								//																  /
	public void testCreateNewUserWithOneRoles() { // test for creating user with one role																				 /	
																						//																				/
		Role roleAdmin = entityManager.find(Role.class, 1);  //entityManager selects the admin role from the table whose id is one and assign it to the roleAdmin<-----/
		User userSauravK = new User("saurav@gmail.com", "sau2020", "Saurav", "Khanal"); //create a user using constructor
		userSauravK.addRoles(roleAdmin);
		
		User savedUser = repo.save(userSauravK);
		assertThat(savedUser.getId()).isGreaterThan(0);	
	} 
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		
		User userDavidS = new User("David@gmail.com", "Dav2020", "David", "Shrestha");
		userDavidS.addRoles(entityManager.find(Role.class, 1));
		userDavidS.addRoles(entityManager.find(Role.class, 3));
		
		User savedUser = repo.save(userDavidS);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
		
	}
	
	@Test
	public void testListAlluser() {
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testDeleteUser() {
		repo.deleteById(7);
	}
	
	@Test
	public void getUserByEmailTest() {
		String email = "rewaj@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountByid() {
		Integer id = 5;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	@Test
	public void testDisableUser() {
		Integer id = 23;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testFindAll() {
		String keyword = "ali";
		Pageable pageable = PageRequest.of(0, 4);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> users = page.getContent();
		
		assertThat(users.size()).isGreaterThan(0);
	}
}
