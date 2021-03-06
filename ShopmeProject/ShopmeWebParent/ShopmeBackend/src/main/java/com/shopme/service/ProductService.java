package com.shopme.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 7;

	@Autowired
	private ProductRepository productRepository;

	public List<Product> listAllProducts() {
		List<Product> findAll = (List<Product>) this.productRepository.findAll();
		return findAll;
	}

	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryID) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		PageRequest pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty()) {

			if (categoryID != null && categoryID > 0) {

				String categoryMatch = "-" + String.valueOf(categoryID) + "-";

//				System.out.println("categoryID is " + categoryID + "  categoryMatch " + categoryMatch);

				return this.productRepository.searchInCategory(categoryID, categoryMatch, keyword, pageable);
			}

			return this.productRepository.findAll(keyword, pageable);
		}

		if (categoryID != null && categoryID > 0) {

			String categoryMatch = "-" + String.valueOf(categoryID) + "-";

			System.out.println("categoryID is " + categoryID + "  categoryMatch " + categoryMatch);

			return this.productRepository.findAllCategory(categoryID, categoryMatch, pageable);
		}

		return this.productRepository.findAll(pageable);

	}

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replace(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replace(" ", "-"));
		}

		product.setUpdatedTime(new Date());
		return this.productRepository.save(product);
	}

	public void saveProductPrice(Product productUsedInForm) {
		Product productInDB = this.productRepository.findById(productUsedInForm.getId()).get();
		productInDB.setCost(productUsedInForm.getCost());
		productInDB.setPrice(productUsedInForm.getPrice());
		productInDB.setDiscountPercentage(productUsedInForm.getDiscountPercentage());
		this.productRepository.save(productInDB);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product product = this.productRepository.findByName(name);
		if (isCreatingNew) {
			if (product != null)
				return "DUPLICATE";
		} else {
			if (product != null && product.getId() != id) {
				return "DUPLICATE";
			}
		}
		return "OK";
	}

	public void isProductEnabled(Integer id, boolean isEnabled) {
		this.productRepository.updateEnabledStatus(id, isEnabled);
	}

	public void delete(Integer id) throws ProductNotFoundException {

		Long countById = this.productRepository.countById(id);

		System.out.println("CountById " + countById);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find product with id: " + id);
		}
		this.productRepository.deleteById(id);
	}

	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return this.productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with ID: " + id);
		}
	}
}
