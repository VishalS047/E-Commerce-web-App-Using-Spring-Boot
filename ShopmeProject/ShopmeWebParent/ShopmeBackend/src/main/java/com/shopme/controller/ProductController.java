package com.shopme.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import com.shopme.exception.ProductNotFoundException;
import com.shopme.service.BrandService;
import com.shopme.service.CategoryService;
import com.shopme.service.ProductService;
import com.shopme.util.FileUploadUtil;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = "/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping(path = "products/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		if (sortDir == null || sortDir.isEmpty())
			sortDir = "asc";

		Page<Product> page = this.productService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts = page.getContent();
		
		List<Category> listCategories = this.categoryService.listCategoryUsedInForm();
		

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

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

		return "products/new_product_form";
	}

	@PostMapping(path = "/products/save")
	public String saveProduct(Product product, RedirectAttributes attributes,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts) throws IOException {

		setMainImageName(mainImageMultipart, product);

		setNewExtraImageNames(extraImageMultiparts, product);

		setExistingExtraImageNames(imageIDs, imageNames, product);

		setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = this.productService.save(product);

		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		deleteExtraImagesThatAreRemovedOnForm(product);

		attributes.addFlashAttribute("message", "The product has been saved successfully");

		return "redirect:/products";
	}

	private void deleteExtraImagesThatAreRemovedOnForm(Product product) {

		String extraImageDirectory = "../product-images/" + product.getId() + "/extras";

		Path dirPath = Paths.get(extraImageDirectory);

		System.out.println("DIRPATH " + dirPath);

		try {
			Files.list(dirPath).forEach(file -> {
				
				String fileName = file.toFile().getName();
				 
				if (!product.containsImageName(fileName)) {
					
					try {
						
						Files.delete(file);
						
						LOGGER.info("Deleted extra image: " + fileName);
						
					} catch (IOException exception) {
						
						LOGGER.error("Could not delete extra image: " + fileName);
						
					}
				}
			});

		} catch (IOException exception) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

		if(imageIDs == null || imageIDs.length == 0)
			return;
 
		Set<ProductImage> images = new HashSet<>();

		for (int i = 0; i < imageIDs.length; i++) {
			Integer id = Integer.parseInt(imageIDs[i]);
			String name = imageNames[i];
			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);
	}

	private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {

		if (detailNames == null || detailNames.length == 0) {
			return;
		}
		if(detailIDs.length == 0 || detailIDs == null) {
			return;
		}
		for (int i = 0; i < detailNames.length; i++) {
			String name = detailNames[i];
			String value = detailValues[i];
			Integer id = Integer.parseInt(detailIDs[i]);
			
			if(id!=0) {
				product.addDetails(id, name, value);
			}
			else if (!name.isEmpty() && !value.isEmpty()) {
					product.addDetails(name, value);
				}
		}
	}

	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {

		if (!mainImageMultipart.isEmpty()) {

			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());

			String uploadDirectory = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDirectory);

			FileUploadUtil.saveFile(uploadDirectory, fileName, mainImageMultipart);

		}

		if (extraImageMultiparts.length > 0) {
			String uploadDirectory = "../product-images/" + savedProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				}
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
			}
		}
	}

	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {

		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {

		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			System.out.println(fileName);
			product.setMainImage(fileName);
		}

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
