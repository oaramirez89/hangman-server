package com.rvt.hangman.server.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.json.Json;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rvt.hangman.server.model.GameStatus;
import com.rvt.hangman.server.model.HangmanGame;
import com.rvt.hangman.server.model.PlayerGuess;

@Component
public class GameService {
	
	private static final Logger logger = LoggerFactory.getLogger(GameService.class);
	
	private Map<String, HangmanGame> activeGames = new HashMap<String, HangmanGame>();
	private WordRepository wordRepo;
	
	public GameService() {
		this.wordRepo = new WordRepository();
	}
	
	public GameService(WordRepository wordRepo) {
		this.wordRepo = wordRepo;
	}

	public String createGame() {
		
		HangmanGame hangmanGame = new HangmanGame();
		hangmanGame.setGameId(RandomStringUtils.randomAlphabetic(6));
		hangmanGame.setStatus(GameStatus.ACTIVE);
		hangmanGame.setWordToGuess(wordRepo.getWordAt(generateRandomNumber(wordRepo.nbrOfWordsInRepo())));
		logger.info("*** Word to guess is: " + hangmanGame.getWordToGuess() + " ***");
		
		String playerGuesses = String.join("", Collections.nCopies(hangmanGame.getWordToGuess().length(), "_"));
		hangmanGame.setPlayerGuesses(playerGuesses);
		
		activeGames.put(hangmanGame.getGameId(), hangmanGame);
		
		return hangmanGame.newFormat().toString();
	}
	
	public String evaluatePlayerGuess(PlayerGuess playerGuess) {
		HangmanGame playerGame = activeGames.get(playerGuess.getGameId());
		
		if (playerGame == null) {
			return Json.createObjectBuilder()
				     .add("error", "Invalid game id")
				     .build().toString();
		}
		
		if (playerGame.getStatus() != GameStatus.ACTIVE) {
			return Json.createObjectBuilder()
				     .add("error", "Game is already complete")
				     .build().toString();
		}
		
		String playerGuesses = playerGame.getPlayerGuesses();
		String wordToGuess = playerGame.getWordToGuess();
		int wordToGuessLength = wordToGuess.length();
		boolean guessMatched = false;
		
		
		/*
		 * I am purposely ignoring when player sends the letter multiple
		 * times. The state of the game will not change as the letter is 
		 * in the word, hence incorrect counter is not incremented.
		 */
		for (int i = 0; i < wordToGuessLength; i++) {
			if (wordToGuess.charAt(i) == playerGuess.getGuess()) {
				StringBuilder guessBuilder = new StringBuilder(playerGuesses);
				guessBuilder.setCharAt(i, playerGuess.getGuess());
				playerGuesses = guessBuilder.toString();
				guessMatched = true;
			}
		}
		
		if (guessMatched) {
			playerGame.setPlayerGuesses(playerGuesses);
		} else {
			playerGame.setIncorrect(playerGame.getIncorrect() + 1);
		}
		
		if (!playerGuesses.contains("_")) {
			playerGame.setStatus(GameStatus.WON);
		}
		
		if (playerGame.getIncorrect() >= 7) {
			playerGame.setStatus(GameStatus.LOST);
		}
		
		return playerGame.gameStatus().toString();
	}
	
	/*
	 *  Randomly pick an index to use to select word 
	 *  from word repo.
	 */
	private int generateRandomNumber(int upperBound) {
		Random random = new Random();
		return random.nextInt(upperBound);
	}
}
