package com.shopme.service;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}

}
