package com.shopme.common.entity;

import java.util.List;

public class SettingBag {
	
	private List<Setting> listSettings;

	public SettingBag(List<Setting> listSettings) {
		super();
		this.listSettings = listSettings;
	}

	public Setting get(String key) {
		int indexOf = listSettings.indexOf(new Setting(key));
		if (indexOf >= 0) {
			return listSettings.get(indexOf);
		}
		return null;
	}

	public String getValue(String key) {
		Setting setting = this.get(key);
		if (setting != null) {
			return setting.getValue();
		}
		return null;
	}

	public void update(String key, String value) {
		Setting setting = get(key);
		if (setting != null && value != null) {
			setting.setValue(value);
		}
	}

	public List<Setting> list() {
		return listSettings;
	}
}
