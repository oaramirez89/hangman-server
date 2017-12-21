package com.rvt.hangman.server.service;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class TestDictUpload {

	@Test
	public void testFileLoad() {
		WordRepository wordRepo = new WordRepository();
		Assert.assertEquals(133252, wordRepo.nbrOfWordsInRepo());

		// create instance of Random class
		Random random = new Random();

		// Generate random index to select a word from dictionary.
		int index = random.nextInt(wordRepo.nbrOfWordsInRepo() + 1);

		System.out.println(String.format("Word at index %d is %s: ", index, wordRepo.getWordAt(index)));

	}
}
