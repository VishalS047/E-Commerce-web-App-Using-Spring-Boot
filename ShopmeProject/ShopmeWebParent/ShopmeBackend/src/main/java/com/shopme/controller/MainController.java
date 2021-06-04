package com.shopme.controller;

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

		return "login";
	}
}
