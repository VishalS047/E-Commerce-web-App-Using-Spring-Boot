package com.shopme.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Category;
import com.shopme.repository.CategoryRespository;


@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryTests {

	@Autowired
	private CategoryRespository catRepo;
	

	@Test
	public void testListEnabledCategories() {
		List<Category> enabledCategories = this.catRepo.findAllEnabled();
		enabledCategories.forEach(category -> {
			System.out.println(category.getName() + " (" + category.isEnabled() + ")");
		});
	}
	
	@Test
	public void findEnabledCategoryByAlias() {
		String str = "analog";
		Category category = this.catRepo.findByEnabledAlias(str);
		assertThat(category).isNotNull();
	}
}
