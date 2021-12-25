package com.shopme.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.user.CurrencyRepository;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.service.SettingService;
import com.shopme.util.FileUploadUtil;

@Controller
public class SettingController {

	@Autowired
	private SettingService settingService;

	@Autowired
	private CurrencyRepository currencyRepository;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listAllSettings = this.settingService.listAllSettings();
		List<Currency> listCurrencies = this.currencyRepository.findAllByOrderByNameAsc();

		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listAllSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "settings/settings";
	}

	@PostMapping(path = "/settings/save_general_settings")
	public String saveGeneralSettings(@RequestParam("fileimage") MultipartFile file,
			HttpServletRequest httpServletRequest, RedirectAttributes attributes) throws IOException {

		GeneralSettingBag settingsBag = this.settingService.getGeneralSettings();

		if (!file.isEmpty()) {
			String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingsBag.updateSiteLogo(value);
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		}

		saveCurrencySymbol(httpServletRequest, settingsBag);
		updateSettingValuesFromForm(httpServletRequest, settingsBag.list());

		attributes.addFlashAttribute("message", "General settings have been saved.");

		return "redirect:/settings";
	}

	public void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag bag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findById = this.currencyRepository.findById(currencyId);
		if (findById.isPresent()) {
			Currency currency = findById.get();
			bag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSetting) {
		for (Setting setting : listSetting) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		this.settingService.saveAll(listSetting);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServer(HttpServletRequest httpServletRequest, RedirectAttributes attributes) {
		List<Setting> mailServerSettings = this.settingService.getMailServerSettings();
		updateSettingValuesFromForm(httpServletRequest, mailServerSettings);
		
		attributes.addFlashAttribute("message", "Mail server settings have been saved");
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSettings(HttpServletRequest httpServletRequest, RedirectAttributes attributes) {
		List<Setting> mailTemplateSettings = this.settingService.getMailTemplateSettings();
		updateSettingValuesFromForm(httpServletRequest, mailTemplateSettings);
		
		attributes.addFlashAttribute("message", "Mail template settings have been saved.");
		return "redirect:/settings";
	}
	
	
}
