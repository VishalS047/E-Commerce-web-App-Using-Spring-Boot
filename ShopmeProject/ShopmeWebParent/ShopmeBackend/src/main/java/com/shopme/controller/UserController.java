package com.shopme.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.service.UserNotFoundException;
import com.shopme.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> allUsers = this.userService.listAll();
		model.addAttribute("allUsers", allUsers);
		return "users";
	}

	@GetMapping(path = "/newuser")
	public String newUserForm(Model model) {
		List<Role> listRoles = this.userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("title", "Shopify - Create New User");
		model.addAttribute("heading", "Create New User");
		return "new_user_registration_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
//		System.out.println(user);

		this.userService.save(user);

		redirectAttributes.addFlashAttribute("message", "The user is saved successfully!");

		return "redirect:/users";
	}

	@GetMapping(path = "/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			User user = this.userService.getUserById(id);
			List<Role> listRoles = this.userService.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("title", "Shopify - Edit User with ID: " + id);
			model.addAttribute("heading", "Edit User with ID: " + id);
			model.addAttribute("listRoles", listRoles);

			return "new_user_registration_form";

		} catch (UserNotFoundException e) {
			System.out.println("ascohasca ahncauhscasc nasuchascn a schasu nascuanscm");
			redirectAttributes.addFlashAttribute("message", e.getMessage());

			return "redirect:/users";
		}
	}

	@GetMapping(path = "/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			this.userService.deleteUser(id);
			redirectAttributes.addFlashAttribute("message", "The User ID " + id + " is successfully deleted. ");
		} catch (UserNotFoundException e) {
//			
			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}
		return "redirect:/users";
	}
}