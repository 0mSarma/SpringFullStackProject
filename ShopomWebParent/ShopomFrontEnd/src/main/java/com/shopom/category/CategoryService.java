package com.shopom.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopom.common.entity.Category;
import com.shopom.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired private CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories(){
		List<Category> listAllEnabledCategories = repo.FindAllEnabled();
		List<Category> listCategoriesWithNoChildren = new ArrayList<>();
		listAllEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if(children == null || children.size() == 0) {
				listCategoriesWithNoChildren.add(category);
			}
		});
		
		return listCategoriesWithNoChildren;
	}
	
	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = repo.findByAliasEnabled(alias);
		if(category == null) {
			throw new CategoryNotFoundException("Category not found of the given alias");
		}
		return category;
	}
	
	public List<Category> getCategoryParents(Category child){
		List<Category> listParents = new ArrayList<>();
		
		Category parent = child.getParent();
		
		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		
		listParents.add(child);
		
		return listParents;
		
	}
}
