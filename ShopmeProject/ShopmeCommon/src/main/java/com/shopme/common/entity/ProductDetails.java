package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Product_Details")
public class ProductDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name = "product_name", nullable = false, length = 255)
	private String name;
	
	@Column(name = "product_value", nullable = false, length = 255)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	
	
	public ProductDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public ProductDetails(String name, String value, Product product) {
		super();
		this.name = name;
		this.value = value;
		this.product = product;
	}



	public ProductDetails(Integer id, String name, String value, Product product) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductDetails [id=" + id + ", name=" + name + ", value=" + value + ", product=" + product + "]";
	}
	
	 

	public ProductDetails(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
		
}
