package com.shopme.common.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends Exception {

	public ProductNotFoundException(String message) {
		super(message);
	}
}
