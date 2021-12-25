package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.user.CountryRepository;
import com.shopme.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private CountryRepository countryRepository;

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "chfcf", roles = "ADMIN")
	public void testListCountries() throws Exception {

		String url = "/countries/list";

		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();

//		System.out.println(jsonResponse);

		Country[] countries = mapper.readValue(jsonResponse, Country[].class);

//		for(Country country : countries)
//			System.out.println(country);

		assertThat(countries).hasSizeGreaterThan(0);

	}

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "chfcf", roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/country/save";
		String countryName = "CHINA";
		Country country = new Country(countryName, "CN");
		MvcResult result = mockMvc.perform(
				post(url).contentType("application/json").content(mapper.writeValueAsString(country)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(jsonResponse);
		Country savedCountry = this.countryRepository.findById(countryId).get();

//		System.out.println(jsonResponse);
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "chfcf", roles = "ADMIN")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/country/save";
		Integer countryId = 2666;
		String countryName = "CHINA";
		Country country = new Country(countryId, countryName, "CIN");
		mockMvc.perform(
				post(url).contentType("application/json").content(mapper.writeValueAsString(country)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(String.valueOf(countryId)));

		Country savedCountry = this.countryRepository.findById(countryId).get();

//		System.out.println(jsonResponse);
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "anything", roles = "ADMIN")
	public void testDeleteCountry() throws Exception {
		Integer countryId = 2666;
		String url = "/country/delete/" + countryId;
		mockMvc.perform(get(url)).andExpect(status().isOk());

		Country country = this.countryRepository.findById(countryId).get();
		assertThat(country).isNull();
	}

}
