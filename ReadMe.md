# CrawlForJobSearch

Project to crawl monster.se for job search and send a mail to specific mail id.

## About

Technology stack used:
* The code is build on Windows environment.
* Selenium webdriver
* PageObjectModel
* Mail sent
* Derby database


## Usage

Add a credentials.properties file in root for the gmail id to be used to send mails.
```
username=<username>
password=<password>
````

Execute the DatabaseService.java class to setup derby database.
```
mvn exec:java@CreateSchema
```
Run below maven command

```
mvn clean install
```


## Authors

* **Mayank Arora** - *Initial work* - [aroramayank2002](https://github.com/aroramayank2002)
