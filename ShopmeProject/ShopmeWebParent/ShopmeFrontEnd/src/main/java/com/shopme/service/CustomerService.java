package com.shopme.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.repository.CustomerRepository;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<Country> listAllCountries() {
		List<Country> findAllByOrderByNameAsc = this.countryRepository.findAllByOrderByNameAsc();
		return findAllByOrderByNameAsc;
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = this.customerRepository.findByEmail(email);
		return customer == null;
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String randomString = RandomString.make(64);
		customer.setVerificationCode(randomString);
		
		this.customerRepository.save(customer);
		
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	public boolean verifyURL(String verificationCode) {
		Customer customer = this.customerRepository.findByVerificationCode(verificationCode);
		if(customer == null || customer.getEnabled()) {
			return false;
		}
		else {
			this.customerRepository.enable(customer.getId());
			return true;
		}
	}
}
