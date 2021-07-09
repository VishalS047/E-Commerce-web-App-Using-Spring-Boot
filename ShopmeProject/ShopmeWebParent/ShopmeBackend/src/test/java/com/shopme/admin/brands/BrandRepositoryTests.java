package com.shopme.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.BrandsRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandsRepository brandsRepo;
	
	@Test
	public void saveBrand() {
		Category laptops = new Category(194);
		Brand acer = new Brand("Acer","acer.png");
		acer.getCategories().add(laptops);
		
		Brand save = brandsRepo.save(acer);
		
		assertThat(save).isNotNull();
		assertThat(save.getBrandId()).isGreaterThan(0);
	}
	
	@Test
	public void saveBrand2() {
		Category mobile = new Category(231);
		Brand apple = new Brand("Apple","apple.png");
		apple.getCategories().add(mobile);
		
		Brand save = brandsRepo.save(apple);
		
		assertThat(save).isNotNull();
		assertThat(save.getBrandId()).isGreaterThan(0);
	}
	
	@Test
	public void saveBrand3() {
		
		Brand samsung = new Brand("Samsung","samsung.png");
		samsung.getCategories().add(new Category(229));
		samsung.getCategories().add(new Category(194));
		
		Brand save = brandsRepo.save(samsung);
		
		assertThat(save).isNotNull();
		assertThat(save.getBrandId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		List<Brand> brands = this.brandsRepo.findAll();
		brands.forEach(System.out::println);
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = this.brandsRepo.findById(290).get();
		samsung.setBrandName(newName);
		Brand save = this.brandsRepo.save(samsung);
		assertThat(save.getBrandName()).isEqualTo(newName);
	}
	
	@Test
	public void testDeleteBrand() {
		Integer id = 287;
		this.brandsRepo.deleteById(id);
		Optional<Brand> findById = this.brandsRepo.findById(id);
		assertThat(findById).isEmpty();
	}
}
