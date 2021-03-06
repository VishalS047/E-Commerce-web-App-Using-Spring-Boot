package com.shopme.admin.products;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void createTestProduct() {
		Brand brand = entityManager.find(Brand.class, 290);
		System.out.println("brand "+brand);
		Category category = entityManager.find(Category.class, 231);
		System.out.println("category "+category);
		
		Product product = new Product();
		product.setName("Samsung S21 ulta");
		product.setAlias("samsung s21 ultra");
		product.setShortDescription("SAMSUNG Galaxy S21 Ultra (Phantom Silver, 256 GB)  (12 GB RAM)");
		product.setFullDescrption("12 GB RAM | 256 GB ROM\r\n" + "17.27 cm (6.8 inch) Quad HD+ Display\r\n"
				+ "108MP + 12MP + 10MP + 10MP | 40MP Front Camera\r\n" + "5000 mAh Lithium-ion Battery\r\n"
				+ "Exynos 2100 Processor");
		product.setCost(105999);
		product.setPrice(128999);
		product.setEnabled(true);
		product.setDiscountPercentage(17);

		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		product.setCategory(category);
		product.setBrand(brand);

		Product savedProduct = this.productRepository.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> findAll = this.productRepository.findAll();

		findAll.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 345;
		Product product = this.productRepository.findById(id).get();
		System.out.println(product);
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 342;
		Product product = this.productRepository.findById(id).get();
		product.setCost(142000);
		this.productRepository.save(product);
		Product updatedProduct = this.entityManager.find(Product.class, id);
		assertThat(updatedProduct.getId()).isEqualTo(id);
	}
	
	@Test
	public void testProductWithImages() {
		Integer productId = 343;
		Product product = this.productRepository.findById(productId).get();
		System.out.println("product: " + product);
		product.setMainImage("main_img.jpg");
		product.addExtraImage("image1.png");
		product.addExtraImage("image2.png");
		product.addExtraImage("image3.png");
		product.addExtraImage("image4.png");
		product.addExtraImage("image5.png");
		
		Product save = this.productRepository.save(product);
		assertThat(save.getImages().size()).isEqualTo(5);
	}
	
	@Test
	public void saveProductWithDetails() {
		Integer productId = 439;
		Product product = this.productRepository.findById(productId).get();
		System.out.println(product);
		product.addDetails("Dedicated Graphic Memory Type", "GDDR6");
		product.addDetails("Dedicated Graphic Memory Capacity", "4 GB");
		product.addDetails("Processor Brand", "AMD");
		product.addDetails("Processor Name", "Ryzen 5 Hexa Core");
		product.addDetails("SSD", "Yes");
		product.addDetails("RAM", "8 GB");
		product.addDetails("RAM TYPE", "DDR4");
		product.addDetails("HDD CAPACITY", "1 TB");
		product.addDetails("PROCESSOR VARIANT", "4600H");
		product.addDetails("CHIPSET", "AMD SoC Platform");
		product.addDetails("Clock Speed", "3.0 GHz with Turbo Boost Upto 4.0 Ghz");
		product.addDetails("Memory Slots", "2 slots");
		product.addDetails("RAM Frequency", "3200 MHz");
		product.addDetails("GRAPHIC PROCESSOR", "NVIDIA GeForce GTX 1650");
		product.addDetails("NUMBER OF CORES", "6 Core");
		Product save = this.productRepository.save(product);
		System.out.println(save.getDetails());
		assertThat(save.getDetails()).isNotEmpty();
	}
}
