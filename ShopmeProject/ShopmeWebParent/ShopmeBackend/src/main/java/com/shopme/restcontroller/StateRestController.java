package com.shopme.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.StateDTO;
import com.shopme.admin.user.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@RestController
public class StateRestController {
	
	@Autowired
	private StateRepository stateRepository;
	
	@GetMapping(path = "/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> listStates =  this.stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<>();
		for(State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}
		return result;
	}
	
	@PostMapping(path = "/state/save")
	public String save(@RequestBody State state) {
		 State savedState = this.stateRepository.save(state);
		return String.valueOf(savedState.getId());
	}
	
	@GetMapping(path = "/state/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		this.stateRepository.deleteById(id);
	}
}
