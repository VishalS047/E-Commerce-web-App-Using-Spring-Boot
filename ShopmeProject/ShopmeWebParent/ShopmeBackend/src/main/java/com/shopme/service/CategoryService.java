package com.shopme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.CategoryRepository;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listAll() {
		List<Category> listRootCategories = this.categoryRepository.listRootCategories();
		return listHierarchicalCategories(listRootCategories);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = rootCategory.getChildren();

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		int sublevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";

			for (int i = 0; i < sublevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, sublevel);
		}
	}

	public List<Category> listCategoryUsedInForm() {

		List<Category> categoriesInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = this.categoryRepository.findAll();

		for (Category category : categoriesInDB) {

			if (category.getParent() == null) {

				categoriesInForm.add(Category.copyIdandName(category));

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {

					String name = "--" + subCategory.getName();

					categoriesInForm.add(Category.copyIdandName(subCategory.getCid(), name));

					listChildren(categoriesInForm, subCategory, 1);
				}
			}

		}
		return categoriesInForm;
	}

	private void listChildren(List<Category> categoriesInForm, Category parent, int sublevel) {
		int subLevel = sublevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";

			for (int i = 0; i < subLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesInForm.add(Category.copyIdandName(subCategory.getCid(), name));
			listChildren(categoriesInForm, subCategory, subLevel);
		}
	}

	public Category saveCategory(Category category) {
		return this.categoryRepository.save(category);
	}
}