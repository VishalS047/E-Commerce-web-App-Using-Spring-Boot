package com.shopme.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.shopme.categorycsvExporter.CategoryCsvExporter;
import com.shopme.categoryinfo.CategoryPageInfo;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.service.CategoryService;
import com.shopme.util.FileUploadUtil;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = "/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, model, null);
	}

	@GetMapping(path = "categories/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, @Param("sortDir") String sortDir, Model model,
			@Param("keyword") String keyword) {

		if (sortDir == null || sortDir.isEmpty())
			sortDir = "asc";

		CategoryPageInfo pageInfo = new CategoryPageInfo();

		List<Category> listCategories = this.categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		long startCount = (pageNum - 1) * this.categoryService.ROOT_CATEGORIES_PER_PAGE + 1;

		long endCount = startCount + this.categoryService.ROOT_CATEGORIES_PER_PAGE - 1;

		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalElements", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "categories/categories";

	}

	@GetMapping(path = "/categories/newcategory")
	public String createForm(Model model) {

		List<Category> categories = this.categoryService.listCategoryUsedInForm();
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categories);
		return "categories/new_category_form";
	}

	@PostMapping(path = "/categories/save")
	public String saveCategory(RedirectAttributes redirectAttributes, Category category,
			@RequestParam("fileimage") MultipartFile image) throws IOException {
		if (!image.isEmpty()) {

			String fileName = StringUtils.cleanPath(image.getOriginalFilename());

			category.setImage(fileName);

			Category savedCategory = this.categoryService.saveCategory(category);

			String uploadDirectory = "../category-images/" + savedCategory.getCid();

			FileUploadUtil.cleanDir(uploadDirectory);

			FileUploadUtil.saveFile(uploadDirectory, fileName, image);
		} else {
			this.categoryService.saveCategory(category);
		}

		redirectAttributes.addFlashAttribute("message", "The category is saved successfully!!");

		return "redirect:/categories";
	}

	@GetMapping(path = "/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Category category = this.categoryService.getCategoryById(id);
			List<Category> listCategory = this.categoryService.listCategoryUsedInForm();

			model.addAttribute("category", category);
			model.addAttribute("title", "Shopify - Edit Category with ID: " + id);
			model.addAttribute("heading", "Edit Category with ID: " + id);
			model.addAttribute("listCategory", listCategory);

//			System.out.println("YO YO YO YO YO YO YO YO");

			return "categories/new_category_form";

		} catch (CategoryNotFoundException e) {
			System.out.println("ascohasca ahncauhscasc nasuchascn a schasu nascuanscm");
			redirectAttributes.addFlashAttribute("message", e.getMessage());

			return "redirect:/categories";
		}
	}

	@GetMapping(path = "/categories/{id}/enabled/{status}")
	public String updateUserStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {

		this.categoryService.updateCategoryEnabledStatus(id, status);

		String message = status ? "enabled" : "disabled";

		redirectAttributes.addFlashAttribute("message", "Category with ID " + id + " " + message);

		return "redirect:/categories";
	}

	@GetMapping(path = "/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
			throws CategoryNotFoundException {

		try {
			this.categoryService.deleteCategory(id);

			String categoryDir = "../category-images/" + id;
			FileUploadUtil.cleanDir(categoryDir);

			redirectAttributes.addFlashAttribute("message", "The Category ID " + id + " is successfully deleted. ");

		} catch (CategoryNotFoundException e) {
//			
			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}

		return "redirect:/categories";
	}

	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {

		List<Category> listAllCategories = this.categoryService.listCategoryUsedInForm(); 

		CategoryCsvExporter exporter = new CategoryCsvExporter();

		exporter.export(listAllCategories, response);
	}
}
