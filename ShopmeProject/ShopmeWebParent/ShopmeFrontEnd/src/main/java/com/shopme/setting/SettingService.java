package com.shopme.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;


	public List<Setting> getGeneralSettings() {
		List<Setting> settings = this.settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		return settings;
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = this.settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(this.settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
		return new EmailSettingBag(settings);
	}
}
