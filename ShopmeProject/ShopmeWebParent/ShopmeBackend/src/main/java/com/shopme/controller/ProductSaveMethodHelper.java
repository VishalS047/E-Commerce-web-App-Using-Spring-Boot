package com.shopme.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import com.shopme.util.FileUploadUtil;

public class ProductSaveMethodHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	public static void deleteExtraImagesThatAreRemovedOnForm(Product product) {

		String extraImageDirectory = "../product-images/" + product.getId() + "/extras";

		Path dirPath = Paths.get(extraImageDirectory);

		System.out.println("DIRPATH " + dirPath);

		try {
			Files.list(dirPath).forEach(file -> {

				String fileName = file.toFile().getName();

				if (!product.containsImageName(fileName)) {

					try {

						Files.delete(file);

						LOGGER.info("Deleted extra image: " + fileName);

					} catch (IOException exception) {

						LOGGER.error("Could not delete extra image: " + fileName);

					}
				}
			});

		} catch (IOException exception) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	public static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();

		for (int i = 0; i < imageIDs.length; i++) {
			Integer id = Integer.parseInt(imageIDs[i]);
			String name = imageNames[i];
			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);
	}

	public static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues,
			Product product) {

		if (detailNames == null || detailNames.length == 0) {
			return;
		}
		if (detailIDs.length == 0 || detailIDs == null) {
			return;
		}
		for (int i = 0; i < detailNames.length; i++) {
			String name = detailNames[i];
			String value = detailValues[i];
			Integer id = Integer.parseInt(detailIDs[i]);

			if (id != 0) {
				product.addDetails(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetails(name, value);
			}
		}
	}

	public static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {

		if (!mainImageMultipart.isEmpty()) {

			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());

			String uploadDirectory = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDirectory);

			FileUploadUtil.saveFile(uploadDirectory, fileName, mainImageMultipart);

		}

		if (extraImageMultiparts.length > 0) {
			String uploadDirectory = "../product-images/" + savedProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				}
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
			}
		}
	}

	public static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {

		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}

	public static void setMainImageName(MultipartFile mainImageMultipart, Product product) {

		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			System.out.println(fileName);
			product.setMainImage(fileName);
		}

	}

}
