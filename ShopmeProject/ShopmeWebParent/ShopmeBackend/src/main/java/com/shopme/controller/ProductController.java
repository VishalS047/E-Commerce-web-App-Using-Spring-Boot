package com.shopme.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.exception.ProductNotFoundException;
import com.shopme.service.BrandService;
import com.shopme.service.ProductService;
import com.shopme.util.FileUploadUtil;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping(path = "/products")
	public String listAllProducts(Model model) {
		List<Product> listAllProducts = this.productService.listAllProducts();
		model.addAttribute("listAllProducts",listAllProducts);
		
		return "products/products";
	}
	
	@GetMapping(path = "/products/newproduct")
	public String newProduct(Model model) {
		List<Brand> listBrands = this.brandService.listAll();
//		System.out.println(listBrands);
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);
		
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("title", "Create new product");
		
		
		return "products/new_product_form";
	}
	
	@PostMapping(path = "/products/save")
	public String saveProduct(Product product,RedirectAttributes attributes,@RequestParam("fileImage") MultipartFile file) throws IOException {
		
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			System.out.println(fileName);
			product.setMainImage(fileName);

			System.out.println("YOYOYO");
			
			Product savedProduct = this.productService.save(product);
			
			String uploadDirectory = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDirectory);
			FileUploadUtil.saveFile(uploadDirectory, fileName, file);
		}else {
			this.productService.save(product);
		}
		
		attributes.addFlashAttribute("message", "The product has been saved successfully");
		return "redirect:/products";
	}
	
	@GetMapping(path = "/product/{id}/enabled/{status}")
	public String checkEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,RedirectAttributes attributes) {
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
			
			attributes.addFlashAttribute("message", "Product with ID "+ id + " successfully deleted");
		} catch (ProductNotFoundException e) {
			// 
			attributes.addFlashAttribute("message",e.getMessage());
		}
		return  "redirect:/products";
	}
	
}