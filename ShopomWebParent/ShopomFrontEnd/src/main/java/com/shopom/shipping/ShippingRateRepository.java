package com.shopom.shipping;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopom.common.entity.Country;
import com.shopom.common.entity.ShippingRate;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
}
