package com.zhizaolian.staff.utils;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.collections4.CollectionUtils;



/**
 * @author 41179076@qq.com
 *
 */
public class EmailSender {
	private static Properties properties;
	private static EmailSender emailSender;

	public synchronized static EmailSender getInstance() {
		if (emailSender == null) {
			emailSender = new EmailSender();
			return emailSender;
		} else {
			return emailSender;
		}
	}

	static {
		PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();
		properties = propertiesHelper.getProperties("email");
	}
	
	public Integer sendEmails(List<String> toList, String title, String content) {
		if (CollectionUtils.isEmpty(toList)) {
			return 0;
		}
		int success = 0;
		for (String string : toList) {
			try {
				sendEmail(string, title, content);
				success++;
			} catch (Exception ignore) {
			}
		}
		return success;
	}

	public void sendEmail(String to, String title, String content) throws Exception {
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String userName = properties.getProperty("userName");
				String password = properties.getProperty("password");
				return new PasswordAuthentication(userName, password);
			}
		};
		Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
		MimeMessage mailMessage = new MimeMessage(sendMailSession);
		mailMessage.setFrom(new InternetAddress(properties.getProperty("from")));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		mailMessage.setSubject(title, "UTF-8");
		mailMessage.setSentDate(new Date());
		Multipart mainPart = new MimeMultipart();
		BodyPart html = new MimeBodyPart();
		html.setContent(content, "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		mailMessage.setContent(mainPart);
		Transport.send(mailMessage);
	}
}
