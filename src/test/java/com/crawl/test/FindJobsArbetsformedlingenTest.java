package com.crawl.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.crawl.test.pageObject.HomePageMonster;
import com.crawl.test.pageObject.SearchArbetsformedlingen;
import com.m.util.GeneralUtil;
import com.m.util.SendMailSSL;

public class FindJobsArbetsformedlingenTest extends BaseTest {
	Logger logger = LoggerFactory.getLogger(FindJobsArbetsformedlingenTest.class);
	SearchArbetsformedlingen arbetsformedlingen;

	private void initializePages() {
		arbetsformedlingen = new SearchArbetsformedlingen(getDriver());
	}

	@BeforeClass
	public void setUp() {
		initializePages();
	}

	@AfterClass
	public void tearDown() {
		logger.info("Close the browser window");
		new SendMailSSL().sendMail(GeneralUtil.getLogFilePath(arbetsformedlingen.getJobLoggerName()), 
				arbetsformedlingen.getJobLoggerName());
	}

	@Test
	public void launchBrowser() {
		logger.info("Launch browser");
		arbetsformedlingen.launchPage("https://www.arbetsformedlingen.se/");
	}

	@Test(dependsOnMethods = { "launchBrowser" })
	public void addKeywordsAndSearch() throws InterruptedException {
		logger.info("Search with keywords");
		arbetsformedlingen.searchForKeywords(
				testProperties.getProperty("search.keywords.arbetsformedlingen"),
				testProperties.getProperty("search.location.monster"));
	}

	@Test(dependsOnMethods = { "addKeywordsAndSearch" })
	public void findAllLinks() throws InterruptedException {
		logger.info("Find job listings");
		logger.info("Nmber of links: " + arbetsformedlingen.getJobs().size());
		Thread.sleep(10000);
	}

}
