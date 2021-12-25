package com.shopme.mvcconfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String dirName = "userphotos";

		Path userPhotosDir = Paths.get(dirName);

		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");

		String categoryImageDirName = "../category-images";

		Path categoryImageDir = Paths.get(categoryImageDirName);

		String categoryImagePath = categoryImageDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoryImagePath + "/");

		String brandLogoDirName = "../brand-logos";

		Path brandLogoDir = Paths.get(brandLogoDirName);
		 
		String brandLogoPath = brandLogoDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandLogoPath + '/');
		
		String productImageDirName = "../product-images";
		
		Path productImageDir = Paths.get(productImageDirName);
		
		String productImagePath = productImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/product-images/**").addResourceLocations("file:/" + productImagePath + '/');
		
		String logoImageDirName = "../site-logo";
		
		Path logoImageDir = Paths.get(logoImageDirName);
		
		String logoImagePath = logoImageDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/site-logo/**").addResourceLocations("file:/" + logoImagePath + '/');
	}

}