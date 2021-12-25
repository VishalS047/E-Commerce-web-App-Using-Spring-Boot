package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.user.CountryRepository;
import com.shopme.admin.user.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "YOYO", roles = "Admin")
	public void testListByCountries() throws Exception {
		Integer countryId = 2661;
		String url = "/states/list_by_country/" + countryId;
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		State[] states = this.mapper.readValue(jsonResponse, State[].class);
		assertThat(states).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "YOYO", roles = "Admin")
	public void testCreateState() throws Exception {
		String url = "/state/save";
		Integer countryId = 2661;
		Country country = this.countryRepository.findById(countryId).get();
		State state = new State("West Bengal", country);
		MvcResult result = mockMvc
				.perform(post(url).contentType("application/json").content(mapper.writeValueAsString(state)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		Optional<State> findById = this.stateRepository.findById(stateId);

		assertThat(findById.isPresent());
	}
	
	@Test
	@WithMockUser(username = "vishal.sharma88055@gmail.com", password = "YOYO", roles = "Admin")
	public void testDeleteState() throws Exception {
		Integer stateId = 2691;
		String url = "/state/delete/" + stateId;
		
		this.mockMvc.perform(get(url)).andExpect(status().isOk());
		
		Optional<State> findById = this.stateRepository.findById(stateId);
		
		assertThat(findById).isNotPresent();
		
	}
	
}
