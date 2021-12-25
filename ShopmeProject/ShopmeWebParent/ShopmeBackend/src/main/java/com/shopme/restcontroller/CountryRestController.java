package com.shopme.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.CountryRepository;
import com.shopme.common.entity.Country;

@RestController
public class CountryRestController {

	@Autowired
	private CountryRepository countryRepository;

	@GetMapping(path = "/countries/list")
	public List<Country> listAll() {
		return this.countryRepository.findAllByOrderByNameAsc();
	}

	@PostMapping(path = "/country/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = this.countryRepository.save(country);
		return String.valueOf(savedCountry.getId());
	}

	@GetMapping(path = "/country/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		this.countryRepository.deleteById(id);
	}
}
