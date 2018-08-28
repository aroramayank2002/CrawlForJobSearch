package com.crawl.test.pageObject;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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

public class HomePageMonster extends BaseTest {
	Logger logger = LoggerFactory.getLogger(HomePageMonster.class);
	String jobLoggerName = "monster";
	Logger jobLogger = LoggerFactory.getLogger(jobLoggerName);
	// AndroidDriver<MobileElement> driver;
	WebDriver driver;
	WebDriverWait wait;
	Integer counter = 0;
	Integer limit;
	final static String JOB_SELECTOR_ATTRIBUTE = "data-jobid";

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
		searchTextField.sendKeys(keyWords);
		locationTextField.sendKeys(location);
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(saveSearchButton));
	}

	private boolean extractData(WebElement webElement) {
		if (this.counter < this.limit) {
			String summary = webElement.findElement(By.className("summary"))
					.getText().replace('\n', ',');
			String url = webElement.findElement(By.tagName("a")).getAttribute(
					"href");
			String datetime = webElement.findElement(By.tagName("time"))
					.getAttribute("datetime");
			// logger.info("Date: " + datetime);
			// logger.info("Summary: " + summary);
			String urlSplit[] = url.split("/");
			// logger.info("Url: " + urlSplit[urlSplit.length-1]);

			long id = dbService.insertUrl(url);
			if (0 == id) {
				logger.warn("Job already shared: " + summary);
				return false;
			} else {
				jobLogger.info(datetime + " | <a href=\"" + url + "\">"
						+ summary + " </a><br/>");
				logger.info("Saving Url: " + urlSplit[urlSplit.length - 1]);
				this.counter++;
				return true;
			}
		} else {
			return false;
		}

	}

	public List<WebElement> getFurtherJobs(int startFrom) {
		moreResults.click();
		try {
			wait.until(ExpectedConditions.visibilityOf(moreResults));
		} catch (WebDriverException ex) {
			logger.error("Couldn't see the next button, means all records are shown now." + ex.getMessage());
			return null;
		}

		List<WebElement> matches = jobSummaries.stream().skip(startFrom)
				.filter(p -> p.getAttribute(JOB_SELECTOR_ATTRIBUTE) != null)
				.collect(Collectors.<WebElement> toList());

		// logger.info("Job banners after filter:" + matches.size());
		return matches;

	}

	public List<WebElement> getJobs() {

		logger.info("Get Jobs");
		logger.info("Current url: " + driver.getCurrentUrl());
		int pageCounter = 0;
		this.counter = 0;
		this.limit = Integer.parseInt(testProperties
				.getProperty("search.jobsLimit"));
		List<WebElement> matches = jobSummaries.stream()
				.filter(p -> p.getAttribute(JOB_SELECTOR_ATTRIBUTE) != null)
				.collect(Collectors.<WebElement> toList());
		int startFrom = matches.size();
		logger.info("Job banners size:" + matches.size() + ", page: "
				+ pageCounter++);
		// matches.stream().forEach(p -> extractData(p));

		List<WebElement> unUsed = matches.stream()
				.filter(match -> extractData(match))
				.collect(Collectors.<WebElement> toList());
		while (this.counter < limit) {
			logger.info("Limit:" + limit + ", counter: " + this.counter);
			List<WebElement> nextPage = getFurtherJobs(startFrom);
			if(null == nextPage){
				break;
			}
			logger.info("Job banners size:" + nextPage.size() + ", page: "
					+ pageCounter++);
			// nextPage.stream().forEach(p -> extractData(p));
			startFrom += nextPage.size();
			unUsed.addAll(matches.stream().filter(match -> extractData(match))
					.collect(Collectors.<WebElement> toList()));
			logger.info("Job unused  size:" + unUsed.size() + ", page: "
					+ pageCounter++);
		}
		logger.info("Limit:" + limit + ", counter: " + this.counter);
		return unUsed;
	}
	
	public String getJobLoggerName(){
		return jobLoggerName;
	}

}
