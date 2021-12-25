package com.shopme.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.service.CustomerService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<Country> listAllCountries = this.customerService.listAllCountries();
		model.addAttribute("listAllCountries", listAllCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());

		return "register/registration_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException, MessagingException {
		
		this.customerService.registerCustomer(customer);
		sendVerificationMail(customer, httpServletRequest);
		model.addAttribute("pageTitle", "Registration Successful");
		return "register/register_success";
	}

	private void sendVerificationMail(Customer customer, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = this.settingService.getEmailSettings();
		JavaMailSender mailSender = Utility.prepareMailSender(emailSettings);
		String toAddress = customer.getEmail();
		String customerVerificationSubject = emailSettings.getCustomerVerificationSubject();
		String customerVerifyContent = emailSettings.getCustomerVerifyContent();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		mimeMessageHelper.setTo(toAddress);
		mimeMessageHelper.setSubject(customerVerificationSubject);
		
		customerVerifyContent =  customerVerifyContent.replace("[[name]]", customer.getFullName());
		
		String verifyURL = Utility.getSiteURL(httpServletRequest) + "/verify?code=" + customer.getVerificationCode();
		
		customerVerifyContent = customerVerifyContent.replace("[[URL]]", verifyURL);
		
		mimeMessageHelper.setText(customerVerifyContent, true);
		
		mailSender.send(mimeMessage);
		
		System.out.println(verifyURL + " " + toAddress);
	}
	
	@GetMapping(path = "/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verifyURL = this.customerService.verifyURL(code);
		return "register/" + (verifyURL == true ? "verify_success" : "verify_fail");
	}
}
