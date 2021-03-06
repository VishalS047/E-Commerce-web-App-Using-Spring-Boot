package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.user.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.service.CategoryService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@MockBean // to create fake object of this repository
	private CategoryRepository categoryRepository;

	@InjectMocks // to inject fake CategoryRepository object to the CategoryService
	private CategoryService categoryService;

	@Test
	public void testCheckUnitInNewModeReturnDuplicateName() {

		Integer id = null;

		String name = "Computers";

		String alias = "abc";

		Category category = new Category(id, name, alias);

		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(category);

		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(null);

		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);

		assertThat(isNameUnique).isEqualTo("NOT_UNIQUE_CATEGORY_NAME");

	}
	
	@Test
	public void testCheckUnitInNewModeReturnDuplicateAlias() {
		
		Integer id = null;
		
		String name = "Yo Yo";
		
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(null);
		
		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);
		
		assertThat(isNameUnique).isEqualTo("NOT_UNIQUE_CATEGORY_ALIAS");
		
	}

	@Test
	public void testCheckUnitInNewModeReturnUNIQUE() {

		Integer id = null;

		String name = "ABC";

		String alias = "YO";

		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(null);

		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(null);

		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);

		assertThat(isNameUnique).isEqualTo("UNIQUE");

	}
	
	@Test
	public void testCheckUnitInEditModeReturnDuplicateName() {

		Integer id = 232;

		String name = "GPU";

		String alias = "gpu";

		Category category = new Category(236, name, alias);

		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(category);

		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(null);

		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);

		assertThat(isNameUnique).isEqualTo("NOT_UNIQUE_CATEGORY_NAME");

	}
	
	@Test
	public void testCheckUnitInEditModeReturnDuplicateAlias() {

		Integer id = 232;
		
		String name = "ABC";

		String alias = "computers";

		Category category = new Category(236, name, alias);

		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(null);

		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(category);

		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);

		assertThat(isNameUnique).isEqualTo("NOT_UNIQUE_CATEGORY_ALIAS");

	}
	
	@Test
	public void testCheckUnitInEditModeReturnUNIQUE() {

		Integer id = 229;

		String name = "ABC";

		String alias = "computers";

		Category category = new Category(id, name, alias);

		Mockito.when(this.categoryRepository.findByName(name)).thenReturn(null);

		Mockito.when(this.categoryRepository.findByAlias(alias)).thenReturn(category);

		String isNameUnique = this.categoryService.checkUniqueness(id, name, alias);

		assertThat(isNameUnique).isEqualTo("UNIQUE");

	}

}
