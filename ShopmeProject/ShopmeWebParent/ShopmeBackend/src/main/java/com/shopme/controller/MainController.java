package com.shopme.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping(path = "/")
	public String viewHomePage() {

		return "index";
	}

	@GetMapping(path = "/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		System.out.println(authentication);
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
			return "login";
		return "redirect:/";
	}
}
