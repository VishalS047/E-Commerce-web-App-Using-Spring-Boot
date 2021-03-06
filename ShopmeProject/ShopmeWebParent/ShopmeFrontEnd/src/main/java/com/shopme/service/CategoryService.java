package com.shopme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.exception.CategoryNotFoundException;
import com.shopme.repository.CategoryRespository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRespository categoryRespository;

	public List<Category> listNoChildrenCategories() {

		List<Category> listNoChildrenCategories = new ArrayList<>();

		List<Category> findAllEnabled = this.categoryRespository.findAllEnabled();

		findAllEnabled.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		});

		return listNoChildrenCategories;
	}

	public Category getEnabledCategory(String alias) throws CategoryNotFoundException {
		Category category = this.categoryRespository.findByEnabledAlias(alias);
		if (category == null) {
			throw new CategoryNotFoundException("could not find category with alias " + alias);
		}
		return category;
	}

	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();

		Category parent = child.getParent();

		System.out.println(parent);

		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);

		return listParents;
	}
}
