package com.m.util;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

public class GeneralUtil {
	static Logger myLogger = LoggerFactory.getLogger(GeneralUtil.class);
	
	public static String getLogFilePath(String loggerName) {
		myLogger.info("Logger name: " + loggerName);
		FileAppender<?> fileAppender = null;
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger
					.iteratorForAppenders(); index.hasNext();) {
				Object enumElement = index.next();
				if (enumElement instanceof FileAppender) {
					fileAppender = (FileAppender<?>) enumElement;
					String name = fileAppender.getName();
					myLogger.info("File appender name: " + name);
					myLogger.info("File appender file: " + fileAppender.getFile());
					if(null != name && name.contains(loggerName)){
						return fileAppender.getFile();
					}
				}
			}
		}
		
		return null;
	}

}
