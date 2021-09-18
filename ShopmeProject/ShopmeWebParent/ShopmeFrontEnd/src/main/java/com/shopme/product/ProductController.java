package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.service.CategoryService;
import com.shopme.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping(path = "/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategoryByPage(alias, model, 1);
	}

	@GetMapping(path = "/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model,
			@PathVariable("pageNum") int pageNum) {

		try {

			Category category = this.categoryService.getEnabledCategory(alias);

			List<Category> categoryParents = this.categoryService.getCategoryParents(category);

			Page<Product> pageProducts = this.productService.listByCategory(category.getCid(), pageNum);

			List<Product> listOfProducts = pageProducts.getContent();

			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}

			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("categoryParents", categoryParents);
			model.addAttribute("catName", category.getName());
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("listOfProducts", listOfProducts);
			model.addAttribute("category", category);

			return "products/products_by_category";
		} catch (Exception e) {

			return "error/404";
		}

	}

	@GetMapping(path = "/p/{p_alias}")
	public String viewProductDetail(@PathVariable("p_alias") String alias, Model model)
			throws ProductNotFoundException {
		try {
			Product product = this.productService.getProduct(alias);
			List<Category> categoryParents = this.categoryService.getCategoryParents(product.getCategory());

			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			model.addAttribute("categoryParents", categoryParents);
			return "products/product_details";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping(path = "/search")
	public String searchFirstPage(@Param("keyword") String keyword, Model model) {
		return searchByPage(keyword, model, 1);
	}
	
	@GetMapping(path = "/search/page/{pageNum}")
	public String searchByPage(@Param("keyword") String keyword, Model model, @PathVariable("pageNum") Integer pageNum) {
		Page<Product> productsPerPage = this.productService.search(keyword, pageNum);
		List<Product> listResult = productsPerPage.getContent();
		
		long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;

		if (endCount > productsPerPage.getTotalElements()) {
			endCount = productsPerPage.getTotalElements();
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("listResult", listResult);
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", productsPerPage.getTotalPages());
		model.addAttribute("totalItems", productsPerPage.getTotalElements());		
		model.addAttribute("pageTitle", keyword + "- Search Result");
		
		return "products/search_result";
	}

}
