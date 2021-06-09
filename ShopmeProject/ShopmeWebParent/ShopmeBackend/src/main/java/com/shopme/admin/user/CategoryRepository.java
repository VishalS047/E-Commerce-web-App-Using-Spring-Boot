package com.shopme.admin.user;

import java.util.List;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	@Query(value = "select c from Category c where c.parent.cid is NULL")
	public List<Category> listRootCategories(Sort sort);
	
	public Category findByName(String name);
	
	public Long countBycid(Integer id);
	
	public Category findByAlias(String alias);

	@Query(value = "update Category c set c.isEnabled=:status where c.cid=:id ")
	@Modifying
	public void updateCategoryStatus(Integer id, boolean status);
}
