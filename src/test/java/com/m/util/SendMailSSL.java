package com.m.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawl.test.BaseTest;

public class SendMailSSL extends BaseTest {
	Logger logger = LoggerFactory.getLogger(SendMailSSL.class);

	public void sendMail(String logFilePath, String siteString) {
		logger.info("Jobs file path: " + logFilePath);
		logger.debug("Username: " + credentials.getProperty("username")
				+ ", Password: " + credentials.getProperty("password"));

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(credentials
								.getProperty("username"), credentials
								.getProperty("password"));
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("hankstomy63@gmail.com"));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(testProperties.getProperty(
							"mail.to", "aroramayank2002@gmail.com")));
			message.setSubject(siteString +" " +testProperties.getProperty("mail.subject"));
			String body = new String(Files.readAllBytes(Paths.get(logFilePath)));
			// String body =
			// "<a href=\"https://jobberbjudande.monster.se/Test-Test-Management-Test-automation-Stockholm-Stockholm-STHM-Sweden-%C3%85F/11/197542963\">ACTIVAR CUENTA</a>";
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(body, "text/html");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			message.setContent(multipart);

			message.setContent(body, "text/html; charset=utf-8");

			Transport.send(message);

			logger.info("Mail sent");

		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	// public static void main(String[] args) throws IOException {
	// new SendMailSSL().sendMail();
	// System.out.println(LocalDate.now());
	// DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	// DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	// DateTimeFormatter formatter = ;
	// System.out.println(DateTimeFormatter.ofPattern("u-MM-dd'T'HH-mm").format(LocalDateTime.now()));
	// }

}