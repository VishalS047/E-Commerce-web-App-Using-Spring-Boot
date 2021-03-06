package com.shopme.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@PostMapping(path="/products/checkUnique")
	public String checkUniqueness(@Param("id")Integer id, @Param("name") String name) {
		return this.productService.checkUnique(id, name);
	}
}
