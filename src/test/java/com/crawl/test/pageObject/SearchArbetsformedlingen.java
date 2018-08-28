package com.crawl.test.pageObject;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class SearchArbetsformedlingen extends BaseTest {
	Logger logger = LoggerFactory.getLogger(SearchArbetsformedlingen.class);
	String jobLoggerName = "arbetsformedlingen";
	Logger jobLogger = LoggerFactory.getLogger(jobLoggerName);
	WebDriver driver;
	WebDriverWait wait;
	Integer counter = 0;
	Integer limit;

	@FindBy(how = How.XPATH, using = "//a[contains(text(), 'Sök jobb')]")
	WebElement searchButton;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(), 'Jag förstår')]")
	WebElement exceptCookies;

	@FindBy(how = How.ID, using = "mp-yrkesroller")
	WebElement searchTextField;
	
	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'Fritextsökning')]")
	WebElement testSearchLabel;

	@FindBy(how = How.ID, using = "ingen-erfarenhet")
	WebElement noExperienceCheckBox;

	@FindBy(how = How.ID, using = "mp-arbetsorter")
	WebElement locationTextField;
	
	@FindBy(how = How.XPATH, using = "//h3[contains(text(), 'Vad du söker')]")
	WebElement locationSearchLabel;
	
	@FindBy(how = How.CLASS_NAME, using = "hittade-annonser")
	WebElement foundJobsCountLabel;

	@FindBy(how = How.CSS, using = ".list-unstyled.spa-dropdown-list")
	WebElement dropDownDiv;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(), 'Publiceringsdatum')]")
	WebElement releaseDateDropDown;

	@FindBy(how = How.XPATH, using = "//a[contains(text(), 'Publicerade senaste dygnet')]")
	WebElement lastDayPublishedDropDown;

	@FindBy(how = How.ID, using = "sort-ordning")
	WebElement firstDropDown;
	
	@FindBy(how = How.CSS, using = ".spa-profile-heading.dropdown-toggle") 
	WebElement listClick;
	
	@FindBy(how = How.ID, using = "meny-dropdown") 
	WebElement secondDropDownList;
	
//	@FindBy(how = How.CSS, using = ".spa-dropdown-menu.dropdown-menu")
//	WebElement secondDropDown;
	
	@FindBy(how = How.CLASS_NAME, using = "pb-spinner-text-sm")
	WebElement spinnerText;
	
	@FindBy(how = How.CLASS_NAME, using = "ng-scope")
	WebElement jobSummariesContainer;

	@FindAll({ @FindBy(how = How.CLASS_NAME, using = "resultatrad") })
	List<WebElement> jobSummaries;

	public SearchArbetsformedlingen(WebDriver driver) {
		this.driver = driver;
		AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(
				driver, 10);
		PageFactory.initElements(factory, this);
		wait = new WebDriverWait(this.driver, 20);
	}

	public void launchPage(String url) {
		this.driver.get(url);
		wait.until(ExpectedConditions.visibilityOf(searchButton));
		exceptCookies.click();
//		jobLogger.info("Today" + " | <a href=\"" + url + "\">Ha ha ha </a><br/>");
	}

	public void searchForKeywords(String keyWords, String location) {
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(searchTextField));
		
		locationTextField.sendKeys(location);
		locationTextField.sendKeys(Keys.RETURN);
		wait.until(ExpectedConditions.invisibilityOf(spinnerText));
		wait.until(ExpectedConditions.visibilityOf(locationSearchLabel));
		
		searchTextField.sendKeys(keyWords);
		searchTextField.sendKeys(Keys.RETURN);
		wait.until(ExpectedConditions.visibilityOf(listClick));
		wait.until(ExpectedConditions.invisibilityOf(spinnerText));
		
		logger.info("Clicking first drop down.");
		listClick.click();
		wait.until(ExpectedConditions.visibilityOf(dropDownDiv));
		releaseDateDropDown.click();
		
		logger.info("Clicking second drop down.");
		wait.until(ExpectedConditions.visibilityOf(firstDropDown));
		secondDropDownList.click();
		wait.until(ExpectedConditions.visibilityOf(lastDayPublishedDropDown));
		lastDayPublishedDropDown.click();
		wait.until(ExpectedConditions.invisibilityOf(spinnerText));
	}

	public List<WebElement> getJobs() {

		logger.info("Get Jobs");
		logger.info("Current url: " + driver.getCurrentUrl());
		int pageCounter = 0;
		this.counter = 0;
		this.limit = Integer.parseInt(testProperties
				.getProperty("search.jobsLimit"));
		// List<WebElement> matches = jobSummaries.stream()
		// .filter(p -> p.getAttribute(JOB_SELECTOR_ATTRIBUTE) != null)
		// .collect(Collectors.<WebElement> toList());
		List<WebElement> matches = jobSummaries;
		int startFrom = matches.size();
		logger.info("Job banners size:" + matches.size() + ", page: "
				+ pageCounter++);
		// matches.stream().forEach(p -> extractData(p));

		List<WebElement> unUsed = matches.stream()
				.filter(match -> extractData(match))
				.collect(Collectors.<WebElement> toList());
		// while (this.counter < limit) {
		// logger.info("Limit:" + limit + ", counter: " + this.counter);
		// List<WebElement> nextPage = getFurtherJobs(startFrom);
		// if(null == nextPage){
		// break;
		// }
		// logger.info("Job banners size:" + nextPage.size() + ", page: "
		// + pageCounter++);
		// // nextPage.stream().forEach(p -> extractData(p));
		// startFrom += nextPage.size();
		// unUsed.addAll(matches.stream().filter(match -> extractData(match))
		// .collect(Collectors.<WebElement> toList()));
		// logger.info("Job unused  size:" + unUsed.size() + ", page: "
		// + pageCounter++);
		// }
		logger.info("Limit:" + limit + ", counter: " + this.counter);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return unUsed;
	}

	private boolean extractData(WebElement webElement) {
		if (this.counter < this.limit) {
			String summary = webElement.findElement(By.className("ng-binding"))
					.getText();
			String url = webElement.findElement(By.className("rubrik"))
					.getAttribute("href");
			String occupation = webElement.findElement(
					By.cssSelector(".yrkesroll.ng-binding")).getText();
			String publishedDate = webElement.findElement(
					By.cssSelector(".publiceringsdatum.ng-binding")).getText();
			String applicationDate = webElement.findElement(
					By.cssSelector(".ansokningsdatum.ng-binding")).getText();

			logger.debug("-------------------------------------------------");
			logger.debug("Summary: " + summary);
			logger.debug("url: " + url);
			logger.debug("occupation: " + occupation);
			logger.debug("publishedDate: " + publishedDate);
			logger.debug("applicationDate: " + applicationDate);
			String urlSplit[] = url.split("/");
			logger.debug("Url: " + urlSplit[urlSplit.length - 1]);

			long id = dbService.insertUrl(url);
			if (0 == id) {
				logger.warn("Job already shared: " + summary);
				return false;
			} else {
				jobLogger.info(publishedDate + " | <a href=\"" + url + "\">"
						+ summary + " </a><br/>");
				logger.info("Saving Url: " + urlSplit[urlSplit.length - 1]);
				this.counter++;
				return true;
			}

		} else {
			return false;
		}
	}
	
	public String getJobLoggerName(){
		return jobLoggerName;
	}
}
