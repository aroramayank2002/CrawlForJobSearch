package com.crawl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

import com.m.util.SendMailSSL;

public abstract class BaseTest {
	Logger logger = LoggerFactory.getLogger(BaseTest.class);
	private static WebDriver service;
	private Properties prop = null;
	private Properties capabilities = null;
	private Properties credentials = null;
	public static final int SLEEP_TIME_3_SEC = 3000;

	private Properties loadProperties(String name) {
		logger.debug("Loading properties: " + name);
		Properties p = new Properties();

		try (InputStream input = new FileInputStream(name)) {
			p.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return p;
	}

	@BeforeSuite
	public void globalSetup() throws IOException {
		prop = loadProperties("test.properties");
		capabilities = loadProperties("capabilities.properties");
		credentials = loadProperties("credentials.properties");
		System.setProperty("webdriver.chrome.driver",
				capabilities.getProperty("webdriver.chrome.driver"));
		service = new ChromeDriver();
	}

	private String getLogFilePath() {
		String logFilePath = null;
		File clientLogFile;
		FileAppender<?> fileAppender = null;
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger
					.iteratorForAppenders(); index.hasNext();) {
				Object enumElement = index.next();
				if (enumElement instanceof FileAppender) {
					fileAppender = (FileAppender<?>) enumElement;
				}
				// System.out.println("logfile path" + enumElement);
			}
		}

		if (fileAppender != null) {
			clientLogFile = new File(fileAppender.getFile());
			System.out
					.println("logfile path" + clientLogFile.getAbsolutePath());
			logFilePath = fileAppender.getFile();
		} else {
			clientLogFile = null;
		}
		return logFilePath;
	}

	@AfterSuite
	public void globalTearDown() {
		service.close();
		new SendMailSSL().sendMail(getLogFilePath());

	}

	public WebDriver getDriver() {
		return service;
	}

	public Properties getTestProperties() {
		if (null == prop) {
			prop = loadProperties("test.properties");
		}
		return prop;
	}

	public Properties getCapabilities() {
		if (null == capabilities) {
			capabilities = loadProperties("capabilities.properties");
		}
		return capabilities;
	}

	public Properties getCredentials() {
		if (null == credentials) {
			credentials = loadProperties("credentials.properties");
		}
		return credentials;
	}

}