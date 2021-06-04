
package com.shopme.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.exception.UserNotFoundException;
import com.shopme.export.UserCsvExporter;
import com.shopme.export.UserExcelExporter;
import com.shopme.export.UserPdfExporter;
import com.shopme.service.UserService;
import com.shopme.util.FileUploadUtil;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listAll(Model model) {
		String page = listByPage(1, model, "firstName", "asc", null);
		return page;
	}

	@GetMapping(path = "/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

//		System.out.println("SortField: "+sortField);
//		
//		System.out.println("SortDir: "+sortDir);
		
		Page<User> page = this.userService.listByPage(pageNum, sortField, sortDir, keyword);

		List<User> allUsers = page.getContent();

//		System.out.println("pageNo " + pno);
//		System.out.println("totalElem " + page.getTotalElements());
//		System.out.println("totalPages " + page.getTotalPages());

//		System.out.println(listUsers);

		
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;

		long endCount = startCount + UserService.USERS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("allUsers", allUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseDir", reverseDir);
		model.addAttribute("keyword", keyword);

		return "users/users";
	}

	@GetMapping(path = "/users/newuser")
	public String newUserForm(Model model) {
		List<Role> listRoles = this.userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("title", "Shopify - Create New User");
		model.addAttribute("heading", "Create New User");
		return "users/new_user_registration_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile file)
			throws IOException {
//		System.out.println(user);
		/*
		 * System.out.println(file.getOriginalFilename());
		 * System.out.println(file.getContentType());
		 * System.out.println(file.getSize()); System.out.println(file.getName());
		 * System.out.println(file.isEmpty());
		 */
		if (!file.isEmpty()) {

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			user.setPhotots(fileName);

			User savedUser = this.userService.save(user);

//			System.out.println("savedUser: " + savedUser);

			String uploadDir = "userphotos/" + savedUser.getId();

//			System.out.println("uploadDir: " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, file);

		} else {
			if (user.getPhotots().isEmpty())
				user.setPhotots(null);

			this.userService.save(user);
		}

//		System.out.println("fileName: "+fileName);

//		

		redirectAttributes.addFlashAttribute("message", "The user is saved successfully!");

		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
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

			return "users/new_user_registration_form";

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

	@GetMapping(path = "/users/{id}/enabled/{status}")
	public String updateUserStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {

		this.userService.updateUserEnabledStatus(id, status);

		String message = status ? "enabled" : "disabled";

		redirectAttributes.addFlashAttribute("message", "User with ID " + id + " " + message);

		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		List<User> listAllUsers = this.userService.listAll();
		
		UserCsvExporter exporter = new UserCsvExporter();
		
		exporter.export(listAllUsers, response);
	}
	
	@GetMapping("/users/export/xlsx")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		List<User> listAllUsers = this.userService.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		
		exporter.export(listAllUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		
		List<User> listAllUsers = this.userService.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		
		exporter.export(listAllUsers, response);
	}
	
}
