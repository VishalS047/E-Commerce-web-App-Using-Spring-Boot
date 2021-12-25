package com.shopme.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.user.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;

	public List<Setting> listAllSettings() {
		return (List<Setting>) this.settingRepository.findAll();
	}

	public GeneralSettingBag getGeneralSettings() {

		List<Setting> setting = new ArrayList<>();
		
		List<Setting> generalSettings = this.settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = this.settingRepository.findByCategory(SettingCategory.CURRENCY);
		
		setting.addAll(generalSettings);
		setting.addAll(currencySettings);
		
		return new GeneralSettingBag(setting);
	}
	
	public void saveAll(Iterable<Setting> setting) {
		this.settingRepository.saveAll(setting);
	}
	
	public List<Setting> getMailServerSettings() {
		return this.settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getMailTemplateSettings() {
		return this.settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
}
