package com.crawl.test;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.m.util.GeneralUtil;
import com.m.util.ParentUtil;
import com.m.util.SendMailSSL;

public class BaseTest extends ParentUtil{
	Logger logger = LoggerFactory.getLogger(BaseTest.class);
	private static WebDriver service;
	public static final int SLEEP_TIME_3_SEC = 3000;

	@BeforeSuite
	public void globalSetup() throws IOException {
		logger.info("Initializing global setup.");
		service = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver",
				capabilities.getProperty("webdriver.chrome.driver"));
	}

	@AfterSuite
	public void globalTearDown() {
		service.close();
		new SendMailSSL().sendMail(GeneralUtil.getLogFilePath());
	}

	public WebDriver getDriver() {
		return service;
	}

	

}