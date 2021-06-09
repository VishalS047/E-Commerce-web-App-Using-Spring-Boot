package com.shopme.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.admin.user.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.exception.CategoryNotFoundException;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		List<Category> listRootCategories = this.categoryRepository.listRootCategories(sort);
		return listHierarchicalCategories(listRootCategories, sortDir);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = this.sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel,
			String sortDir) {
		int sublevel = subLevel + 1;
		Set<Category> children = this.sortSubCategories(parent.getChildren(), sortDir);
		for (Category subCategory : children) {
			String name = "";

			for (int i = 0; i < sublevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, sublevel, sortDir);
		}
	}

	public List<Category> listCategoryUsedInForm() {

		List<Category> categoriesInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = this.categoryRepository.listRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {

			if (category.getParent() == null) {

				categoriesInForm.add(Category.copyIdandName(category));

				Set<Category> children = this.sortSubCategories(category.getChildren());

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
		Set<Category> children = this.sortSubCategories(parent.getChildren());
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

	public Category getCategoryById(Integer id) throws CategoryNotFoundException {
		try {
			return this.categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find user with ID: " + id);
		}
	}

	public String checkUniqueness(Integer id, String name, String alias) {

		boolean isCategoryNew = (id == null || id == 0);

		Category categoryByName = this.categoryRepository.findByName(name);

		Category categoryByAlias = this.categoryRepository.findByAlias(alias);

		if (isCategoryNew) {
			if (categoryByName != null) {
				return "NOT_UNIQUE_CATEGORY_NAME";

			} else {
				if (categoryByAlias != null) {
					return "NOT_UNIQUE_CATEGORY_ALIAS";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getCid() != id) {
				return "NOT_UNIQUE_CATEGORY_NAME";
			} else {
				if (categoryByAlias != null && categoryByAlias.getCid() != id) {
					return "NOT_UNIQUE_CATEGORY_ALIAS";
				}
			}
		}

		return "UNIQUE";
	}

	public SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	public SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				// TODO Auto-generated method stub
				if (sortDir.equals("asc"))
					return cat1.getName().compareTo(cat2.getName());
				else
					return cat2.getName().compareTo(cat1.getName());
			}
		});

		sortedChildren.addAll(children);
		return sortedChildren;
	}

	public void updateCategoryEnabledStatus(Integer id, boolean status) {
		// TODO Auto-generated method stub
		this.categoryRepository.updateCategoryStatus(id, status);
	}

	public void deleteCategory(Integer id) throws CategoryNotFoundException {
		Long countById = this.categoryRepository.countBycid(id);
		
		if (countById == null || countById == 0) {
			System.out.println("YSCABISCBSICASCUASCISUC");
			throw new CategoryNotFoundException("Could not find any user with id: " + id);
		}
		
		this.categoryRepository.deleteById(id);
	}
}
