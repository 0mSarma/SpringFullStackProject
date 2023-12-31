package com.shopom.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopom.common.entity.Role;

@DataJpaTest  //indicates this as a test class
@AutoConfigureTestDatabase(replace = Replace.NONE)	 //annotation to run the test against the real database not in memory database 
@Rollback(false)  //not reverting any changes made to the database during the test.
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testFirstRoleCreation() {
		Role roleAdmin = new Role("Admin", "Manages Everything");
		Role submitRole = repo.save(roleAdmin);
		
		assertThat(submitRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson", "Manages product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manages catagories, brands, products");
		Role roleShipper = new Role("Shipper", "view products view orders ");
		Role roleAssistant = new Role("Assistant", "Manages questions and reviews");
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}

}
