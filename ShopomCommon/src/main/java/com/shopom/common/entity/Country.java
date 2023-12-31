package com.shopom.common.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity {
	
	@Column(nullable = false, length = 45)
	private String name;
	
	@Column(nullable = false, length = 45)
	private String code;
	
	@OneToMany(mappedBy = "country",fetch = FetchType.EAGER)
	private Set<State> states;
	
	
	public Country() {
	
	}


	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}


	public Country(String name, String code, Set<State> states) {
		this.name = name;
		this.code = code;
		this.states = states;
	}
	
	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}


	public Country(String countryName) {
		this.name = countryName;
	}

	public Country(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + ", states=" + states + "]";
	}
	
	
}
