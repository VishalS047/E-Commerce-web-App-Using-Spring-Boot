package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettngRepositoryTests {

	@Autowired
	private SettingRepository settingRepository;

	@Test
	public void testCreateGeneralSettings() {
//		Setting setting = new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyRightInformation = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopify Ltd.",
				SettingCategory.GENERAL);
		this.settingRepository.saveAll(List.of(siteLogo, copyRightInformation));

		Iterable<Setting> findAll = this.settingRepository.findAll();
		assertThat(findAll).size().isGreaterThan(0);
	}

	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSTION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSAND_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

		Iterable<Setting> saveAll = this.settingRepository.saveAll(
				List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));

		assertThat(saveAll).size().isGreaterThan(0);
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> findByCategory = this.settingRepository.findByCategory(SettingCategory.GENERAL);
		findByCategory.forEach(System.out::println);
	}
}
