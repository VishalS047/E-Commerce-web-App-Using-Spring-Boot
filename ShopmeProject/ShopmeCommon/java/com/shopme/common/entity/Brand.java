package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Brands_Table")
public class Brand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer brandId;
	
	@Column(nullable = false, length = 50)
	private String brandName;
	
	@Column(nullable = false, length = 128) 
	private String logo;
	
	@ManyToMany
	@JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_Id"), inverseJoinColumns = @JoinColumn(name = "category_Id"))
	private Set<Category> categories = new HashSet<>();

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public Brand(String brandName,String logo) {
		super();
		this.brandName = brandName;
		this.logo = logo;
	}

	public Brand(Integer brandId, String brandName) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLogo() {
		return logo;
	}

	public Brand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Brand(Integer brandId, String brandName, String logo, Set<Category> categories) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.logo = logo;
		this.categories = categories;
	}

	public Brand(String name) {
		// TODO Auto-generated constructor stub
		this.brandName = name;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	@Transient
	public String getLogoPath() {
		if(this.brandId == null) {
			return "/images/brand-image.jpg";
		}
		return "/brand-logos/" + this.getBrandId() + "/" + this.logo;
	}

	@Override
	public String toString() {
		return "Brand [brandId=" + brandId + ", brandName=" + brandName + ", logo=" + logo + ", categories="
				+ categories + "]";
	}
	
	
	
}
