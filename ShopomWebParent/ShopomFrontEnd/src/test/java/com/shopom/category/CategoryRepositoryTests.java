package com.shopom.category;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopom.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {
	
	@Autowired private CategoryRepository repo;
	
	@Test
	public void testListEnabledCategories() {
		List<Category> categoryEnabledList = repo.FindAllEnabled();
		categoryEnabledList.forEach(category -> {
			System.out.println(category.getName());
		});
	}
	
	@Test
	public void testFindCategoryByAlias() {
		String alias = "Somthing";
		Category category = repo.findByAliasEnabled(alias);
		
		assertThat(category).isNotNull();
	}

}
