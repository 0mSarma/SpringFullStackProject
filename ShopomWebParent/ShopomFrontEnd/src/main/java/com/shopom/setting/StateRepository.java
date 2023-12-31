package com.shopom.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopom.common.entity.Country;
import com.shopom.common.entity.State;

public interface StateRepository extends JpaRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
}
