package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

public class Utility {

	public static String getSiteURL(HttpServletRequest httpServletRequest) {
		String siteURL = httpServletRequest.getRequestURL().toString();
		return siteURL.replace(httpServletRequest.getServletPath(), "");
	}

	public static JavaMailSender prepareMailSender(EmailSettingBag emailSettingBag) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailSettingBag.getHost());
		mailSender.setPort(emailSettingBag.getPort());
		mailSender.setUsername(emailSettingBag.getUserName());
		mailSender.setPassword(emailSettingBag.getPassword());

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", emailSettingBag.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", emailSettingBag.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);

		return mailSender;
	}
}
