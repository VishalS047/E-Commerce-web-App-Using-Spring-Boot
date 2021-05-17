package com.shopme.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	public PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		List<User> listAllUsers = (List<User>) this.userRepository.findAll();
		return listAllUsers;
	}

	public List<Role> listRoles() {
		List<Role> findAllRoles = (List<Role>) this.roleRepository.findAll();
		return findAllRoles;
	}

	public void save(User user) {
		boolean isUpdatingUser = (user.getId() != 0);

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

		this.userRepository.save(user);
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
		if(countById == null || countById ==0) {
			throw new UserNotFoundException("Could not find any user with id: "+id);
		}
		
		this.userRepository.deleteById(id);
	}
}