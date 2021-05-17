package com.shopme.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan({ "com.shopme.common.entity", "com.shopme.admin.user" })
@ComponentScan({ "com.shopme.controller", "com.shopme.service", "com.shopme.security", "com.shopme.restcontroller" })
public class ShopmeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeBackendApplication.class, args);
	}
}