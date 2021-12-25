package com.shopme.admin.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.CurrencyRepository;
import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyTest {
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Test
	public void saveCurrency() {
		Currency currency = new Currency("United States Dollar", "$", "USD");
		Currency currency2 = new Currency("British Pound", "£", "GBP");
		Currency currency3 = new Currency("Japanese Yen", "¥", "JPY");
		Currency currency4 = new Currency("Euro", "€", "EUR");
		Currency currency5 = new Currency("Russian Rubble", "₱", "RUB");
		Currency currency6 = new Currency("South Korean Won", "₩", "KRW");
		Currency currency7 = new Currency("Chinese Yuan", "¥", "CNY");
		Currency currency8 = new Currency("Brazilian Real", "R$", "BRL");
		Currency currency9 = new Currency("Australian Dollar", "$", "AUD");
		Currency currency10 = new Currency("Canadian Dollar", "$", "CAD");
		Currency currency11 = new Currency("Vietnamese đồng", "₫", "VND");
		Currency currency12 = new Currency("Indian Rupee", "₹", "INR");
		
		List<Currency> list = List.of(currency, currency2, currency3, currency4, currency5, currency6, currency7, currency8, currency9, currency10, currency11, currency12);
		this.currencyRepository.saveAll(list);
		Iterable<Currency> findAll = this.currencyRepository.findAll();
		
		assertThat(findAll).size().isEqualTo(12);
	}
	
	@Test
	public void listByOrderByNameAsc() {
		List<Currency> currencies = this.currencyRepository.findAllByOrderByNameAsc();
		currencies.forEach((s) -> System.out.println(s));
		assertThat(currencies).size().isEqualTo(12);
	}
}
