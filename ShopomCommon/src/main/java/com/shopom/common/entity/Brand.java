package com.shopom.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name= "brands")
public class Brand extends IdBasedEntity {
	
	
	@Column(length = 45, nullable = false, unique = true)
	private String name;
	
	@Column(length =140, nullable = false)
	private String logo;
	
	@ManyToMany										//an auxiliary table (join table) is created in the database to store the associations between the two entities.
	@JoinTable(name = "brands_categories",	// This specifies the name of the newly created join table						
			   joinColumns = @JoinColumn( name = "brand_id" ), //the name of the column in the join table that will be used to reference the owning side of the relationship.
			   inverseJoinColumns = @JoinColumn( name = "category_id")  // This specifies the column in the join table that represents the "Category" entity's side of the relationship. 
			)
	private Set<Category> categories = new HashSet<>();  //

	public Brand( String name, String logo) {
		this.name = name;
		this.logo = logo;
	}
	
	
	
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
	}



	public Brand() {
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}


	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}
	
	@Transient
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";
		
		return "/brand-logos/" + this.id + "/" + this.logo;
	}
	
}
