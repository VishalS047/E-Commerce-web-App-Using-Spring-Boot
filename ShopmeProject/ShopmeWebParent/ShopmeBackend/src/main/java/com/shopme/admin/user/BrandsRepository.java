package com.shopme.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;

public interface BrandsRepository extends PagingAndSortingRepository<Brand, Integer>{
	
	public Long countByBrandId(Integer id);
	
	public Brand findByBrandName(String name);
	
	@Query(value = "select b from Brand b where b.brandName LIKE %?1%")
	public Page<Brand> findAll(String keyword,Pageable pageable);
	
	@Query(value = "SELECT NEW Brand(b.brandId,b.brandName) from Brand b ORDER BY b.brandName ASC")
	public List<Brand> findAll();
}
