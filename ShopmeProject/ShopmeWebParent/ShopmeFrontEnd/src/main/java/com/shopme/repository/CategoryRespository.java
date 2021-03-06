package com.shopme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRespository extends CrudRepository<Category, Integer> {

	@Query("Select c from Category c where c.isEnabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();
	
	@Query("Select c from Category c where c.isEnabled = true and c.alias = ?1")
	public Category findByEnabledAlias(String alias);
}
