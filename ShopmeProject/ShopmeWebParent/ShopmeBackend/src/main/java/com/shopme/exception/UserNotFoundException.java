package com.shopme.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}

}
