package com.shopme.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> listAllProducts() {
		List<Product> findAll = (List<Product>)this.productRepository.findAll();
		return findAll;
	}
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replace(" ", "-");
			product.setAlias(defaultAlias);
		}
		else {
			product.setAlias(product.getAlias().replace(" ", "-"));
		}
		
		product.setUpdatedTime(new Date());
		return this.productRepository.save(product);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product product = this.productRepository.findByName(name);
		if(isCreatingNew) {
			if(product != null) 
				return "DUPLICATE";
		}
		else {
				if(product!=null && product.getId() != id) {
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
		
		System.out.println("CountById "+ countById);
		
		if(countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find product with id: "+id);
		}
		this.productRepository.deleteById(id);
	}
}