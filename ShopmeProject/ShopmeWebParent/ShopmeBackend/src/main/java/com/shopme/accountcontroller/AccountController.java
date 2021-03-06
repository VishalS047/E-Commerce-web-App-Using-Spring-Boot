package com.shopme.accountcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.User;
import com.shopme.security.ShopifyUserDetails;
import com.shopme.service.UserService;
import com.shopme.util.FileUploadUtil;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	@GetMapping(path = "/account")
	public String viewDetails(@AuthenticationPrincipal ShopifyUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		User user = this.userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user,@AuthenticationPrincipal ShopifyUserDetails loggedUser, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile file)
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

			User savedUser = this.userService.updateAccount(user);

//			System.out.println("savedUser: " + savedUser);

			String uploadDir = "userphotos/" + savedUser.getId();

//			System.out.println("uploadDir: " + uploadDir);

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, file);

		} else {
			if (user.getPhotots().isEmpty())
				user.setPhotots(null);

			this.userService.updateAccount(user);
		}

//		System.out.println("fileName: "+fileName);
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLasrName(user.getLastName());

		redirectAttributes.addFlashAttribute("message", "Your account details have been updated successfully!");

		return "redirect:/account";
	}

}
