package com.shopme.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.user.BrandsRepository;
import com.shopme.common.entity.Brand;
import com.shopme.service.BrandService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

	
	@MockBean
	private BrandsRepository brandsRepository;
	
	@MockBean
	private BrandService brandService;
	
	@Test
	public void testCheckUniqueInNewMethodReturnDuplicate() {
		Integer id = null;
		String name = "Acer";
		Brand brand = new Brand(name);
		Mockito.when(this.brandsRepository.findByBrandName(name)).thenReturn(brand);
		String result = this.brandService.checkUnique(id, name);
		assertThat(result).isEqualTo("DUPLICATE");
	}
	
	@Test
	public void testCheckUniqueInNewMethodReturnOk() {
		Integer id = null;
		String name = "Lenovo";
		
		Mockito.when(this.brandsRepository.findByBrandName(name)).thenReturn(null);
		
		String result = this.brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("NEW");
	}
	
	
	@Test
	public void testCheckUniqueInEditMethodReturnDuplicate() {
		Integer id = 307;
		String name = "Calvin Klein";
		
		Brand brand = new Brand(id, name);
		
		Mockito.when(this.brandsRepository.findByBrandName(name)).thenReturn(brand);
		
		String result = this.brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("DUPLICATE");
	}
	
	@Test
	public void testCheckUniqueInEditMethodReturnOk() {
		Integer id = 307;
		String name = "Calvin Klein";
		
		Brand brand = new Brand(id, name);
		
		Mockito.when(this.brandsRepository.findByBrandName(name)).thenReturn(brand);
		
		String result = this.brandService.checkUnique(290, name);
		
		assertThat(result).isEqualTo("OK");
	}
	
	
	
}
