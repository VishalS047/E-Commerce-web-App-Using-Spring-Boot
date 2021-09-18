package com.shopme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("Select p from Product p where p.enabled = true and p.category.cid = ?1 OR p.category.allParentIDs LIKE %?2% ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

	public Product findByAlias(String alias);

	@Query(value = "SELECT * from Products_Table WHERE enabled = 1 and name LIKE %?1% "
			+ "or full_description LIKE %?1% or short_description LIKE %?1%", nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);
}
