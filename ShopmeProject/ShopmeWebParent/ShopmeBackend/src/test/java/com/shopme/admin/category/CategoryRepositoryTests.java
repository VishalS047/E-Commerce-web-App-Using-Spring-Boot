package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void testCreateRootCategory() {

		Category category = new Category("Electronics");
		Category savedCategory = this.categoryRepository.save(category);

		assertThat(savedCategory.getCid()).isGreaterThan(0);

	}

	@Test
	public void testCreateSubCategory() {
		
		Category parent = new Category(197);
		Category subCategory = new Category("Memory",parent);
//		Category smartPhones = new Category("Smart Phones",parent);
		
//		this.categoryRepository.saveAll(List.of(cameras,smartPhones));
		Category savedCategory = this.categoryRepository.save(subCategory);

		assertThat(savedCategory.getCid()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category category = this.categoryRepository.findById(194).get();
		
		System.out.println("categoryName: "+category.getName());
		
		Set<Category> children = category.getChildren();
		
//		System.out.println("children: "+children);
		
		for(Category subCategory : children) 
			System.out.println(subCategory.getName());
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = this.categoryRepository.findAll();
		
		for(Category category: categories) {
			if(category.getParent() == null) {
				System.out.println("Categories: "+category.getName());
				
				Set<Category> children = category.getChildren();
				for(Category subCategory : children) {
					System.out.println("--"+subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
			
		}
			
	}
	
	private void printChildren(Category parent, int sublevel) {
		int subLevel = sublevel+1;
		Set<Category> children = parent.getChildren();
		for(Category subCategory :children) {
			for(int i = 0; i<subLevel ;i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printChildren(subCategory, subLevel);
		}
	}
	
	@Test
	public void selectRootCategory() {
		List<Category> listRootCategories = this.categoryRepository.listRootCategories(Sort.by("name").ascending());
		listRootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void findByName() {
		String name = "Computers";
		Category categoryName = this.categoryRepository.findByName(name);
		
		assertThat(categoryName).isNotNull();
		assertThat(categoryName.getName()).isEqualTo(name);
	}
	
	@Test
	public void findByAlias() {
		String alias = "mobile";
		Category categoryAlias = this.categoryRepository.findByAlias(alias);
		
		assertThat(categoryAlias).isNotNull();
		assertThat(categoryAlias.getAlias()).isEqualTo(alias);
	}
}
