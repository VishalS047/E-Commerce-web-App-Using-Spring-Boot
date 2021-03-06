package com.shopme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Category;
import com.shopme.service.CategoryService;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = "/")
	public String viewHomePage(Model model) {

		List<Category> listCategories = this.categoryService.listNoChildrenCategories();

//		System.out.println("LISTCATEGORIES" + listCategories);

		model.addAttribute("listCategories", listCategories);
		
		model.addAttribute("yoyo", "Vishal");

		return "index";
	}
}
