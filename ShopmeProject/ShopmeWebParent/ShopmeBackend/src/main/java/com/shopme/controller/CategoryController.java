package com.shopme.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Category;
import com.shopme.service.CategoryService;
import com.shopme.util.FileUploadUtil;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping(path = "/categories")
	public String listAll(Model model) {
		List<Category> listCategories = this.categoryService.listAll();
		model.addAttribute("listCategories",listCategories);
		return "categories/categories";
	}
	
	@GetMapping(path = "/categories/newcategory")
	public String createForm(Model model) {
		
		List<Category> categories = this.categoryService.listCategoryUsedInForm();
		model.addAttribute("category",new Category());
		model.addAttribute("categories",categories);
		return "categories/new_category_form";
	}
	
	@PostMapping(path = "/categories/save")
	public String saveCategory(RedirectAttributes redirectAttributes , Category category , @RequestParam("fileimage") MultipartFile image) throws IOException {
		String fileName = StringUtils.cleanPath(image.getOriginalFilename());
		
		category.setImage(fileName);
		
		Category savedCategory = this.categoryService.saveCategory(category);
		
		String uploadDirectory = "../category-images/" + savedCategory.getCid();
		
		FileUploadUtil.saveFile(uploadDirectory, fileName, image);
		
		redirectAttributes.addFlashAttribute("message",savedCategory.getName() + " category is saved!!");
		
		return "redirect:/categories";
	}

}