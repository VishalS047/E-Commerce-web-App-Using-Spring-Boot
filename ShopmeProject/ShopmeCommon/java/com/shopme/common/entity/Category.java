package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Categories_Table")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer cid;

	@Column(length = 128, nullable = false, unique = false)
	private String name;

	@Column(length = 64, nullable = false, unique = false)
	private String alias;

	@Column(length = 128, nullable = false)
	private String image;

	private boolean isEnabled;

	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public static Category copyIdandName(Category category) {
		Category category2 = new Category();
		category2.setCid(category.getCid());
		category2.setName(category.getName());
		return category2;
	}

	public static Category copyIdandName(Integer id, String name) {
		Category category2 = new Category();
		category2.setCid(id);
		category2.setName(name);
		return category2;
	}

	public static Category copyFull(Category category) {

		Category category2 = new Category();
		category2.setCid(category.getCid());
		category2.setName(category.getName());
		category2.setImage(category.getImage());
		category2.setAlias(category.getAlias());
		category2.setEnabled(category.isEnabled());
		category2.setHasChildren(category.getChildren().size() > 0);
		return category2;
	}

	public static Category copyFull(Category category, String name) {

		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);

		return copyCategory;
	}

	public Category(Integer cid) {
		super();
		this.cid = cid;
	}

	public Category(String name) {
		super();
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category() {

	}

	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Category getParent() {
		return parent;
	}

	public Category(Integer cid, String name, String alias) {
		super();
		this.cid = cid;
		this.name = name;
		this.alias = alias;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	@Transient
	public String getPhotosImagePath() {
		if (this.cid == null)
			return "/images/download.png";
//		System.out.println("/userphotos/" + this.id + "/" + this.photots);
		return "/category-images/" + this.cid + "/" + this.image;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	@Transient
	private boolean hasChildren;

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}
