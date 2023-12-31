package com.shopom.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopom.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired private CurrencyRepository repo;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
				new Currency("Nepali Rupees", "रु‎", "NPR"),
				new Currency("Indian Rupees",  "रु‎", "INR"),
				new Currency("Australian Dollar", "$", "AUD"),
				new Currency("United States Dollar", "$", "USD")
		);
		
		repo.saveAll(listCurrencies);
		Iterable<Currency> iterable = repo.findAll();
		assertThat(iterable).size().isEqualTo(4);			
	}
	
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		assertThat(currencies.size()).isGreaterThan(0);
	}
}
