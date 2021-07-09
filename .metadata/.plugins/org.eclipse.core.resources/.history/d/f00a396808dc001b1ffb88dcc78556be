package com.shopme.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.BrandsRepository;
import com.shopme.common.entity.Brand;
import com.shopme.exception.BrandNotFoundException;

@Service
public class BrandService {

	@Autowired
	private BrandsRepository brandsRepository;

	public static final int BRANDS_PER_PAGE = 5;

	public List<Brand> listAll() {
		List<Brand> listAllBrands = (List<Brand>) this.brandsRepository.findAll();
		return listAllBrands;
	}

	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		PageRequest pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
		
		System.out.println(pageNum);
		
		if (keyword != null) {
			return this.brandsRepository.findAll(keyword, pageable);
		}
		return this.brandsRepository.findAll(pageable);
	}

	public Brand save(Brand brand) {
		Brand save = this.brandsRepository.save(brand);

		return save;
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			Brand brand = brandsRepository.findById(id).get();
			return brand;
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			throw new BrandNotFoundException("Could not find brand with the given id " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countByBrandId = this.brandsRepository.countByBrandId(id);
		if (countByBrandId == null || countByBrandId == 0) {
			throw new BrandNotFoundException("Could not find brand with the given id ");
		}
		this.brandsRepository.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brand = this.brandsRepository.findByBrandName(name);
		if (isCreatingNew) {
			if (brand != null) {
				return "DUPLICATE";
			}
		} else {
			if (brand != null && brand.getBrandId() != id) {
				return "DUPLICATE";
			}
		}

		return "NEW";
	}

}
