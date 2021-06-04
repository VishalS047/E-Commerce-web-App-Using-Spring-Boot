package com.shopme.service;

import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.exception.UserNotFoundException;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 6;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	public PasswordEncoder passwordEncoder;
	
	public User getUserByEmail(String email) {
		User user = this.userRepository.getUserByEmail(email);
		return user;
	}
	
	public List<User> listAll() {
		List<User> listAllUsers = (List<User>) this.userRepository.findAll(Sort.by("firstName").ascending());
		return listAllUsers;
	}

	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

		if (keyword != null) {

			Page<User> allUsers = this.userRepository.findAll(keyword, pageable);
			return allUsers;
		}

		Page<User> allUsers = this.userRepository.findAll(pageable);

		// System.out.println(allUsers);

		return allUsers;
	}

	public List<Role> listRoles() {
		List<Role> findAllRoles = (List<Role>) this.roleRepository.findAll();
		return findAllRoles;
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User exsistingUser = this.userRepository.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {

				user.setPassword(exsistingUser.getPassword());

			} else {

				this.encodePassword(user);
			}
		} else {
			this.encodePassword(user);
		}

		return this.userRepository.save(user);
	}
	
	public User updateAccount(User userInForm) {
		User userInDB = this.userRepository.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			this.encodePassword(userInDB);
		}
		
		if(userInForm.getPhotots() != null) {
			userInDB.setPhotots(userInForm.getPhotots());
		}
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		User user = this.userRepository.save(userInDB);
		return user;
	}

	public void encodePassword(User user) {
		String password = user.getPassword();
		String encodedPassword = this.passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = this.userRepository.getUserByEmail(email);

		if (userByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find user with ID: " + id);
		}
	}

	public void deleteUser(Integer id) throws UserNotFoundException {
		Long countById = this.userRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with id: " + id);
		}

		this.userRepository.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {

		this.userRepository.updateEnabledStatus(id, enabled);
	}

}
