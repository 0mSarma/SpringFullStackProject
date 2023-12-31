package com.shopom.admin.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopom.common.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

	public List<Country> findAllByOrderByNameAsc();
}
