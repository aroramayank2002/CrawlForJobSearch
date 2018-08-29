package com.crawl.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.crawl.test.pageObject.HomePageMonster;
import com.m.util.GeneralUtil;
import com.m.util.SendMailSSL;

public class FindJobsMonsterTest extends BaseTest {
	Logger logger = LoggerFactory.getLogger(FindJobsMonsterTest.class);
	HomePageMonster homePageMonster;

	private void initializePages() {
		homePageMonster = new HomePageMonster(getDriver());
	}

//	@BeforeClass
//	public void setUp() {
//		initializePages();
//	}
//
//	@AfterClass
//	public void tearDown() {
//		logger.info("Close the browser window");
//		new SendMailSSL().sendMail(
//				GeneralUtil.getLogFilePath(homePageMonster.getJobLoggerName()),
//				homePageMonster.getJobLoggerName());
//	}
//
//	@Test
//	public void launchBrowser() {
//		logger.info("Launch browser");
//		homePageMonster.launchPage("https://www.monster.se/");
//	}
//
//	@Test(dependsOnMethods = { "launchBrowser" })
//	public void addKeywordsAndSearch() {
//		logger.info("Search with keywords");
//		homePageMonster.searchForKeywords(
//				testProperties.getProperty("search.keywords.monster"),
//				testProperties.getProperty("search.location.monster"));
//	}
//
//	@Test(dependsOnMethods = { "addKeywordsAndSearch" })
//	public void findAllLinks() throws InterruptedException {
//		logger.info("Find job listings");
//		logger.info("Nmber of links: " + homePageMonster.getJobs().size());
//		// Thread.sleep(10000);
//	}

}
