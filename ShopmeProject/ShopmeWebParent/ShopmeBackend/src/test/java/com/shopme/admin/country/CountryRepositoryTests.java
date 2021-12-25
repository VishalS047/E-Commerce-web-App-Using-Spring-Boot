package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.CountryRepository;
import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {

	@Autowired
	private CountryRepository countryRepository;

	@Test
	public void testCreateCountry() {
		Country country = this.countryRepository.save(new Country("India", "IN"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCountries() {
		List<Country> allCountries = this.countryRepository.findAllByOrderByNameAsc();
		allCountries.forEach(System.out::println);
		assertThat(allCountries.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCountry() {
		Integer id = 2661;
		String name = "India";
		Country country = this.countryRepository.findById(id).get();
		country.setName(name);
		Country updatedCountry = this.countryRepository.save(country);
		assertThat(updatedCountry.getName()).isEqualTo(name);
	}
	
	@Test
	public void getCountry() {
		Integer id = 2661;
		Country country = this.countryRepository.findById(id).get();
		assertThat(country).isNotNull();
	}
	
	@Test
	public void testDeleteCountry() {
		Integer id = 2661;
		this.countryRepository.deleteById(id);
		Country country = this.countryRepository.findById(id).get();
		assertThat(country).isNull();
	}
}
