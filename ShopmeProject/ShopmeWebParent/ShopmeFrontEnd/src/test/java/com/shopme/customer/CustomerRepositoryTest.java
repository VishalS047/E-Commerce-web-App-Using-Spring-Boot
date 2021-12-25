package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.repository.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 2661;
		Country country = this.entityManager.find(Country.class, countryId);

		Customer customer = new Customer();

		customer.setEmail("vishal.sharma88055@gmail.com");
		customer.setPassword("testing");
		customer.setCountry(country);
		customer.setFirstName("Vishal");
		customer.setLastName("Sharma");
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAddress_line1("R.N. Colony");
		customer.setAddress_line2("New Bus Stand");
		customer.setPhoneNumber("9800890361");
		customer.setPinCode("765204");
		customer.setCity("Siliguri");
		customer.setState("West Bengal");
		customer.setVerificationCode("YOYOYO");

		Customer savedCustomer = this.customerRepository.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void listCustomers() {
		List<Customer> allCustomers = (List<Customer>)this.customerRepository.findAll();
//		allCustomers.forEach(System.out::println);
		System.out.println(allCustomers);
		assertThat(allCustomers).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 2718;
		String lastName = "Sharma yo";
		Customer customer = this.customerRepository.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(false);
		Customer savedCustomer = this.customerRepository.save(customer);
		assertThat(savedCustomer.getId()).isEqualTo(customerId);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 2718;
		Customer customer = this.customerRepository.findById(customerId).get();
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2718;
		this.customerRepository.deleteById(customerId);
		Customer customer = this.customerRepository.findById(customerId).get();
		assertThat(customer).isNotNull();
	}
	
	@Test
	public void testfindByEmail() {
		String email = "vishal.sharma88055@gmail.com";
		Customer customer = this.customerRepository.findByEmail(email);
		assertThat(customer).isNotNull();
	}
}
