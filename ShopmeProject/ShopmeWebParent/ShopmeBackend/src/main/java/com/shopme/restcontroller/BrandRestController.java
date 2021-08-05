package com.shopme.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.exception.BrandNotFoundRestException;
import com.shopme.exception.BrandNotFoundException;
import com.shopme.service.BrandService;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService brandService;
	
	@PostMapping(path = "/brands/check_unique")
	public String checkUniqueness(@Param("brandId") Integer id, @Param("brandName") String name) {
		System.out.println("JAI SHREE RAM");
		return this.brandService.checkUnique(id, name);
	}
	
	@GetMapping(path = "/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Integer brandId) throws BrandNotFoundRestException {
		
		List<CategoryDTO> listCategories = new ArrayList<>();
		
		try {
			
			Brand brand = this.brandService.get(brandId);
			
			Set<Category> categories = brand.getCategories();
			
			for(Category category : categories) {
				
				CategoryDTO categoryDTO = new CategoryDTO(category.getCid(),category.getName());
				
				listCategories.add(categoryDTO);
				
			}
			return listCategories;
			
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
