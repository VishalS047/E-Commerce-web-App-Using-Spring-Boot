package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 2661;
		Country country = this.entityManager.find(Country.class, countryId);
		State state = this.stateRepository.save(new State("West Bengal", country));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		Integer stateId = 2662;
		String stateName = "NOIDA";
		State state = this.stateRepository.findById(stateId).get();
		state.setName(stateName);
		State updatedState = this.stateRepository.save(state);
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
}
