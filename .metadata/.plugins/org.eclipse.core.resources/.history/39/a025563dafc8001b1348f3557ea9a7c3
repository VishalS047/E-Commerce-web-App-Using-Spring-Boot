package com.shopme.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.service.CategoryService;

@RestController
public class CategoryRestController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping(path = "/categories/isUnique")
	public String checkUnique(@Param("cid") Integer id, @Param("name") String name, @Param("alias") String alias) {
		return categoryService.checkUniqueness(id, name, alias);
	}
}
