package com.m.util;

import org.testng.TestNG;

import com.crawl.test.FindJobsMonsterTest;

public class TestNGRunner {

	public static void main(String[] args) {
		TestNG runner=new TestNG();

		Class[] classes = {FindJobsMonsterTest.class};
		runner.setTestClasses(classes);

		// finally execute the runner using run method
		runner.run();

	}

}
