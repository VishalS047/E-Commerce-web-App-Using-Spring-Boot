package com.shopme.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;

	@PostMapping(path = "/users/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id,@Param("email") String email) {
		return this.userService.isEmailUnique(id,email) ? "OK" : "Duplicate";
	}
}
