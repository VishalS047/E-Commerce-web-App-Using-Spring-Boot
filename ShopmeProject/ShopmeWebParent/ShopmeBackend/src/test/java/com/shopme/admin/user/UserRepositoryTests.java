package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreateUser() {

		Role roleAdmin = testEntityManager.find(Role.class, 72);
		User userVishal = new User("iamvishal047@gmail.com", "P@55c0de", "Vishal", "Sharma");
		userVishal.addRole(roleAdmin);
		User savedUser = this.userRepository.save(userVishal);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userVishal = new User("vs9800890@gmail.com", "P@55c0de", "Jai", "Shree Ram");
		Role roleEditor = new Role(70);
		Role roleAssistant = new Role(71);

		userVishal.addRole(roleEditor);
		userVishal.addRole(roleAssistant);

		User savedUser = this.userRepository.save(userVishal);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void listAllUsers() {
		List<User> listUsers = (List<User>) this.userRepository.findAll();
		for (User user : listUsers)
			System.out.println(user);
		assertThat(listUsers.size()).isGreaterThan(0);
	}

	@Test
	public void getUserById() {
		User userVishal = this.userRepository.findById(77).get();
		System.out.println(userVishal);
		assertThat(userVishal).isNotNull();
	}

	@Test
	public void updateUserDetails() {
		User user = this.userRepository.findById(77).get();
		user.setEnabled(true);
		user.setEmail("vs+9800890@gmail.com");
		this.userRepository.save(user);
	}

	@Test
	public void testUpdateUserRoles() {
		User user = this.userRepository.findById(77).get();
		Role roleEditor = new Role(72);
		Role roleAssistant = new Role(74);
		user.getRoles().remove(roleEditor);
		user.getRoles().remove(roleAssistant);

		this.userRepository.save(user);
	}

	@Test
	public void TestDeleteUser() {
		int uid = 77;
		this.userRepository.deleteById(uid);
	}

	@Test
	public void getUserByEmail() {
		String email = "iamvishal047@gmail.com";
		User user1 = this.userRepository.getUserByEmail(email);

//		System.out.println(user1);

		assertThat(user1).isNotNull();
	}

	@Test
	public void testCountBy() {
		Integer id = 77;
		Long countById = this.userRepository.countById(id);
		System.out.println(countById);
		assertThat(countById).isNotNull();
	}
	
	@Test
	public void testDisabledUser() {
		Integer id = 121;
		this.userRepository.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testEnabledUser() {
		Integer id = 121;
		this.userRepository.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pagesize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pagesize);
		
		Page<User> page = this.userRepository.findAll(pageable);
		
		List<User> listContent = page.getContent();
		
		listContent.forEach(user ->System.out.println(user));
		
		assertThat(listContent.size()).isEqualTo(pagesize);
	}
	
	@Test
	public void testSearchModule() {
		
		String keyword = "Sh";
		
		int pageNumber = 0;
		int pagesize = 5;
		
		Pageable pageable = PageRequest.of(pageNumber, pagesize);
		
		Page<User> page = this.userRepository.findAll(keyword,pageable);
		
		List<User> listContent = page.getContent();
		
		listContent.forEach(user ->System.out.println(user));
		
		assertThat(listContent.size()).isGreaterThan(0);
	}
	
	@Test
	public void checkPassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.matches("Vishal", "$2a$10$J8/jJ3KbXgKeWNn2VZvA3.553odqx9tE6.iAweYb3NSjDSu6Z2B16"));
	}
}
