package com.shopme.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.security.ShopifyUserDetails;
import com.shopme.service.BrandService;
import com.shopme.service.CategoryService;
import com.shopme.service.ProductService;
import com.shopme.util.FileUploadUtil;

@Controller
public class ProductController {

	

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = "/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null, 0);
	}
	
	@GetMapping(path = "products/page/{pageNum}")
	public String listByPage(
			@PathVariable("pageNum") int pageNum, Model model,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryID") Integer categoryID) {

		
//		System.out.println("categoryID" + categoryID);
		
		if (sortDir == null || sortDir.isEmpty())
			sortDir = "asc";
		
		Page<Product> page = this.productService.listByPage(pageNum, sortField, sortDir, keyword, categoryID);
		List<Product> listProducts = page.getContent();
		
		List<Category> listCategories = this.categoryService.listCategoryUsedInForm();
		

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

//		System.out.println("PageNum: " + pageNum);

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if(categoryID != null) {
			model.addAttribute("categoryID", categoryID);
		}

		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currPage", pageNum);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("listAllProducts", listProducts);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);

		return "products/products";
	}

	@GetMapping(path = "/products/newproduct")
	public String newProduct(Model model) {
		List<Brand> listBrands = this.brandService.listAll();
		System.out.println(listBrands);
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("title", "Create new product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/new_product_form";
	}

	@PostMapping(path = "/products/save")
	public String saveProduct(Product product, RedirectAttributes attributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopifyUserDetails loggedUser) throws IOException {

		
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				attributes.addFlashAttribute("message", "The product has been saved successfully.");			
				return "redirect:/products";
			}
		}
		
		ProductSaveMethodHelper.setMainImageName(mainImageMultipart, product);

		ProductSaveMethodHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		
		ProductSaveMethodHelper.setNewExtraImageNames(extraImageMultiparts, product);

		ProductSaveMethodHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = this.productService.save(product);

		ProductSaveMethodHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		ProductSaveMethodHelper.deleteExtraImagesThatAreRemovedOnForm(product);

		attributes.addFlashAttribute("message", "The product has been saved successfully");

		return "redirect:/products";
	}
	
	
	@GetMapping(path = "/product/{id}/enabled/{status}")
	public String checkEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes attributes) {
		this.productService.isProductEnabled(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		attributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping(path = "/product/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
		try {
			this.productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.cleanDir(productExtraImagesDir);
			FileUploadUtil.cleanDir(productImagesDir);

			attributes.addFlashAttribute("message", "Product with ID " + id + " successfully deleted");
		} catch (ProductNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping(path = "/product/edit/{id}")
	public String editProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes attributes)
			throws ProductNotFoundException {
		
		try {
			Product product = this.productService.get(id);
			
			List<Brand> listAllBrands = this.brandService.listAll();

			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("title", "Edit Product ID: " + id);
			model.addAttribute("listBrands", listAllBrands);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/new_product_form";

		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			attributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping(path = "/product/detail/{id}")
	public String viewProductDetails(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes attributes)
			throws ProductNotFoundException {
		
		try {
			
			Product product = this.productService.get(id);
			 
			model.addAttribute("product", product);
			
			return "products/view-product-detail-modal";
			
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			attributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
}
