package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	
	public Product findByName(String name);
	
	@Query("UPDATE Product p set p.enabled = ?2 where p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public Long	countById(Integer id);
	
	@Query("SELECT p from Product p where p.name LIKE %?1%  OR p.shortDescription LIKE %?1% OR p.fullDescrption LIKE %?1% OR"
			+ " p.brand.brandName LIKE %?1% OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
}
