package com.m.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParentUtil {
	Logger logger = LoggerFactory.getLogger(ParentUtil.class);
	protected Properties testProperties = null;
	protected Properties capabilities = null;
	protected Properties credentials = null;
	protected DatabaseService dbService;
	
	public ParentUtil() {
		testProperties = loadProperties("test.properties");
		capabilities = loadProperties("capabilities.properties");
		credentials = loadProperties("credentials.properties");		
		dbService = new DatabaseService();
	}
	
	private Properties loadProperties(String name) {
		logger.info("Loading properties: " + name);
		Properties p = new Properties();

		try (InputStream input = new FileInputStream(name)) {
			p.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return p;
	}

}
