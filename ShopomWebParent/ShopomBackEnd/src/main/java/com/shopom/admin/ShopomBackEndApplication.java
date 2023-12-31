package com.shopom.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopom.common", "com.shopom.admin.user"}) //lets the program know where the entities are
public class ShopomBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopomBackEndApplication.class, args);
	}

}
