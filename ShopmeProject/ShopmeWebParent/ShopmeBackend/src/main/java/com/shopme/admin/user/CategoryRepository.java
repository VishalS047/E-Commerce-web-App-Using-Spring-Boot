package com.shopme.admin.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query(value = "select c from Category c where c.parent.cid is NULL")
	public List<Category> listRootCategories();
}