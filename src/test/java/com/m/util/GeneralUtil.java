package com.m.util;

import java.io.File;
import java.util.Iterator;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

public class GeneralUtil {
	
	public static String getLogFilePath() {
		String logFilePath = null;
		File clientLogFile;
		FileAppender<?> fileAppender = null;
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger
					.iteratorForAppenders(); index.hasNext();) {
				Object enumElement = index.next();
				if (enumElement instanceof FileAppender) {
					fileAppender = (FileAppender<?>) enumElement;
				}
				// System.out.println("logfile path" + enumElement);
			}
		}

		if (fileAppender != null) {
			clientLogFile = new File(fileAppender.getFile());
			System.out
					.println("logfile path" + clientLogFile.getAbsolutePath());
			logFilePath = fileAppender.getFile();
		} else {
			clientLogFile = null;
		}
		return logFilePath;
	}

}
