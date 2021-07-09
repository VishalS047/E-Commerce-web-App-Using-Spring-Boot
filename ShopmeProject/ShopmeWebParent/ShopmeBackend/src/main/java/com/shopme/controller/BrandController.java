package com.shopme.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.exception.BrandNotFoundException;
import com.shopme.service.BrandService;
import com.shopme.service.CategoryService;
import com.shopme.util.FileUploadUtil;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(path = "/brands", method = RequestMethod.GET)
	public String brand(Model model) {

		List<Brand> listAllBrands = this.brandService.listAll();

		model.addAttribute("listAllBrands", listAllBrands);

		return listByPage(1, model, "brandName", "asc", null);
	}

	@GetMapping(path = "brands/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		if (sortDir == null || sortDir.isEmpty())
			sortDir = "asc";

		Page<Brand> page = this.brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> content = page.getContent();

		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		System.out.println("PageNum: " + pageNum);

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currPage", pageNum);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("listAllBrands", content);
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}

	@GetMapping(path = "/brands/newbrand")
	public String newBrand(Model model) {
		Brand brand = new Brand();
		List<Category> listCategoryUsedInForm = categoryService.listCategoryUsedInForm();

		model.addAttribute("listAllCategories", listCategoryUsedInForm);
		model.addAttribute("brand", brand);
		model.addAttribute("title", "Create new Brand");
		return "brands/new_brand_add_form";
	}

	@PostMapping(path = "/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileimage") MultipartFile file, RedirectAttributes attributes)
			throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			System.out.println(fileName);
			brand.setLogo(fileName);

			System.out.println("YOYOYO");
			Brand savedBrand = this.brandService.save(brand);
			String uploadDirectory = "../brand-logos/" + savedBrand.getBrandId();
			FileUploadUtil.cleanDir(uploadDirectory);
			FileUploadUtil.saveFile(uploadDirectory, fileName, file);
		} else {
			this.brandService.save(brand);
		}

		attributes.addFlashAttribute("message", "Brand has been saved successfully");
		return "redirect:/brands";
	}

	@GetMapping(path = "/brands/edit/{id}")
	public String editBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
		try {
			Brand brand = this.brandService.get(id);
			List<Category> listCategoryUsedInForm = this.categoryService.listCategoryUsedInForm();
			model.addAttribute("brand", brand);
			model.addAttribute("listAllCategories", listCategoryUsedInForm);
			model.addAttribute("title", "Edit Brand with ID: " + id);

			return "brands/new_brand_add_form";
		} catch (BrandNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping(path = "/brands/delete/{id}")
	public String deleteBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
		try {
			this.brandService.delete(id);
			String brandDirectory = "../brand-logos/" + id;
			FileUploadUtil.cleanDir(brandDirectory);

			attributes.addFlashAttribute("message", "Brand with ID: " + id + " deleted successfully");

		} catch (BrandNotFoundException e) {
			// TODO: handle exception
			attributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/brands";
	}
}
