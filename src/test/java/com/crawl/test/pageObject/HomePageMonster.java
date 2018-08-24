package com.crawl.test.pageObject;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawl.test.BaseTest;
import com.m.util.DatabaseService;

public class HomePageMonster extends BaseTest{
	Logger logger = LoggerFactory.getLogger(HomePageMonster.class);
	Logger jobLogger = LoggerFactory.getLogger("details");
	// AndroidDriver<MobileElement> driver;
	WebDriver driver;
	WebDriverWait wait;
	Integer counter = 0;
	Integer limit;
	
	

	@FindBy(how = How.XPATH, using = "//h1[contains(text(), 'Sök Jobb')]")
	WebElement head;

	@FindAll({ @FindBy(tagName = "a") })
	List<WebElement> anchors;

	@FindBy(how = How.ID, using = "q1")
	WebElement searchTextField;

	@FindBy(how = How.ID, using = "where1")
	WebElement locationTextField;

	@FindBy(how = How.ID, using = "doQuickSearch")
	WebElement searchButton;

	@FindBy(how = How.ID, using = "job-alert-flyout")
	WebElement saveSearchButton;

	@FindAll({ @FindBy(how = How.CLASS_NAME, using = "card-content") })
	List<WebElement> jobSummaries;
	
	@FindAll({ @FindBy(how = How.ID, using = "loadMoreJobs") })
	WebElement moreResults;
	
//	@FindAll({ @FindBy(how = How.ID, using = "SearchResults") })
//	WebElement resultsContainer;
	
	public HomePageMonster(WebDriver driver) {
		this.driver = driver;
		AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(
				driver, 10);
		PageFactory.initElements(factory, this);
		wait = new WebDriverWait(this.driver, 20);
	}

	public void launchPage(String url) {
		this.driver.get(url);
		wait.until(ExpectedConditions.visibilityOf(head));
	}

	public void searchForKeywords(String keyWords, String location) {
		// TODO Auto-generated method stub
		searchTextField.sendKeys(keyWords);
		locationTextField.sendKeys(location);
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(saveSearchButton));
	}
	
	private boolean extractData(WebElement webElement){
		if(this.counter<this.limit){
			String summary = webElement.findElement(By.className("summary")).getText().replace('\n', ',');
			String url = webElement.findElement(By.tagName("a")).getAttribute("href");
			String datetime = webElement.findElement(By.tagName("time")).getAttribute("datetime");
	//		logger.info("Date: " + datetime);
	//		logger.info("Summary: " + summary);
			String urlSplit[] = url.split("/");
//			logger.info("Url: " + urlSplit[urlSplit.length-1]);
			
			long id = DatabaseService.insertUrl(url);
			if(0 == id){
				logger.warn("Job already shared: " + summary);
				return false;
			}else{
				jobLogger.info(datetime + " | <a href=\"" + url + "\">" + summary + " </a><br/>"  );
				logger.info("Saving Url: " + urlSplit[urlSplit.length-1]);
				this.counter++;
				return true;
			}
		}else{
			return false;
		}
		
	}
	
	public List<WebElement> getFurtherJobs(int startFrom) {
		moreResults.click();
//		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOf(moreResults));
//		PageFactory.initElements(driver, this);
		List<WebElement> matches = jobSummaries.stream().skip(startFrom)
				.filter(p -> p.getAttribute("data-jobid") != null)
				.collect(Collectors.<WebElement> toList());
		
//		logger.info("Job banners after filter:" + matches.size());
		return matches;
		
	}

	public List<WebElement> getJobs() {
		
		logger.info("Get Jobs");
		logger.info("Current url: "+driver.getCurrentUrl());
		int pageCounter = 0;
		this.counter = 0;
		this.limit = Integer.parseInt(getTestProperties().getProperty("search.jobsLimit"));
		List<WebElement> matches = jobSummaries.stream()
				.filter(p -> p.getAttribute("data-jobid") != null)
				.collect(Collectors.<WebElement> toList());
		int startFrom = matches.size();
		logger.info("Job banners size:" + matches.size()+", page: " + pageCounter++);
//		matches.stream().forEach(p -> extractData(p));
		
		List<WebElement> unUsed = matches.stream().filter(match -> extractData(match) ).collect(Collectors.<WebElement>toList());
		while(this.counter < limit){
			logger.info("Limit:" + limit+", counter: " + this.counter);
			List<WebElement> nextPage = getFurtherJobs(startFrom);
			logger.info("Job banners size:" + nextPage.size()+", page: " + pageCounter++);
//			nextPage.stream().forEach(p -> extractData(p));
			startFrom += nextPage.size();
			unUsed.addAll(matches.stream().filter(match -> extractData(match) ).collect(Collectors.<WebElement>toList()));
			logger.info("Job unused  size:" + unUsed.size()+", page: " + pageCounter++);
		}
		logger.info("Limit:" + limit+", counter: " + this.counter);
		return unUsed;
	}

}
