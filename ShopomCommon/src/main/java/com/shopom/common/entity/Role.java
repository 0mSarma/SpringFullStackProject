package com.shopom.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity // jpa annotation to represent an entity class
@Table(name = "roles") // annotation used to mapp to the table in the database
public class Role extends IdBasedEntity{
	
	@Column(length = 40, nullable = false, unique = true) //annotaion to make sure name characters are of length 40, not null, and are unique 
	private String name;
	
	@Column(length = 150, nullable = false)
	private String description;
	
	public Role() {
	}
	
	public Role(String name) {
		super();
		this.name = name;
	}

	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
