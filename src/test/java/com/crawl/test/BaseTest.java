package com.crawl.test;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.m.util.ParentUtil;

public class BaseTest extends ParentUtil{
	Logger logger = LoggerFactory.getLogger(BaseTest.class);
	private static WebDriver service;
	public static final int SLEEP_TIME_3_SEC = 3000;

	@BeforeSuite
	public void globalSetup() throws IOException {
		logger.info("Initializing global setup.");
		
		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("disable-infobars");
		service = new ChromeDriver(chromeOptions);
		String driverPath = capabilities.getProperty("webdriver.chrome.driver");
		String osName = System.getProperty("os.name");
		if(osName.contains("Windows")){
			driverPath += ".exe";
		}		
		System.setProperty("webdriver.chrome.driver", driverPath);
	}

	@AfterSuite
	public void globalTearDown() {
		service.close();
	}

	public WebDriver getDriver() {
		return service;
	}

	

}