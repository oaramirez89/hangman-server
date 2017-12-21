package com.rvt.hangman.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class WordRepository {

	private static final Logger logger = LoggerFactory.getLogger(WordRepository.class);

	private String fileName = "cmudict.txt";

	private List<String> words = new ArrayList<String>();
	
	/*
	 *  This constructor is used strictly for testing.
	 */
	public WordRepository(List<String> words) {
		this.words = words;
	}

	public WordRepository() {
		Stream<String> lines = null;
		
		try {
			ClassPathResource resource = new ClassPathResource(fileName);
			InputStream inputStream = resource.getInputStream();
			lines = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")).lines();
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}
			
		lines
			.map(s -> s.trim().split(" ")[0].toLowerCase())
			.filter(s -> !s.isEmpty())
			.forEach(words::add);
		
		logger.info("**** Loaded word dictionary. Count: " + words.size() + " *****");
	}

	public String getWordAt(int index) {
		return words.get(index);
	}

	public int nbrOfWordsInRepo() {
		return words.size();
	}
}
