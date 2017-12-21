package com.rvt.hangman.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rvt.hangman.server.model.PlayerGuess;

public class TestEvaluatePlayerGuess {
	private JsonObject jsonObj = null;
	GameService gameService = null;
	
	@Before
	public void setup() {
		// Setup ensures words list only has one entry
		// so we can have a consistent test.
		List<String> words = new ArrayList<String>();
		words.add("total");
		
		WordRepository wordRepo = new WordRepository(words);
		gameService = new GameService(wordRepo);
		
		jsonObj = convertToObj(gameService.createGame());
	}
	
	@Test
	public void testWordIsCorrectLength() {
		String playerGuesses = jsonObj.getString("word");
		// length of word 'total' is 5
		Assert.assertEquals(5, playerGuesses.length());
		Assert.assertEquals("_____", playerGuesses);
	}
	
	@Test
	public void testPlayerGuessCorrect() {
		PlayerGuess playerGuess = new PlayerGuess();
		String gameId = jsonObj.getString("gameId");
		playerGuess.setGameId(gameId);
		playerGuess.setGuess('t');
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals(0, responseObj.getInt("incorrect"));
		Assert.assertEquals("t_t__", responseObj.getString("word"));
		
	}
	
	@Test
	public void testPlayerGuessIncorrect() {
		PlayerGuess playerGuess = new PlayerGuess();
		String gameId = jsonObj.getString("gameId");
		playerGuess.setGameId(gameId);
		playerGuess.setGuess('u');
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals(1, responseObj.getInt("incorrect"));
		Assert.assertEquals("_____", responseObj.getString("word"));
		
	}
	
	@Test
	public void testPlayerLost() {
		PlayerGuess playerGuess = new PlayerGuess();
		String gameId = jsonObj.getString("gameId");
		playerGuess.setGameId(gameId);
		playerGuess.setGuess('u');
		
		// Invoke 7 times then check response
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals(7, responseObj.getInt("incorrect"));
		Assert.assertEquals("LOST", responseObj.getString("status"));
		Assert.assertEquals("_____", responseObj.getString("word"));
	}
	
	@Test
	public void testPlayerWins() {
		PlayerGuess playerGuess = new PlayerGuess();
		String gameId = jsonObj.getString("gameId");
		playerGuess.setGameId(gameId);
		playerGuess.setGuess('t');
		
		// Complete word 'total' then check response
		// status is 'WIN' and incorrect count is 0.
		gameService.evaluatePlayerGuess(playerGuess);
		playerGuess.setGuess('o');
		gameService.evaluatePlayerGuess(playerGuess);
		playerGuess.setGuess('a');
		gameService.evaluatePlayerGuess(playerGuess);
		playerGuess.setGuess('l');
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals(0, responseObj.getInt("incorrect"));
		Assert.assertEquals("WON", responseObj.getString("status"));
		Assert.assertEquals("total", responseObj.getString("word"));
	}
	
	@Test
	public void testGameOverError() {
		PlayerGuess playerGuess = new PlayerGuess();
		String gameId = jsonObj.getString("gameId");
		playerGuess.setGameId(gameId);
		playerGuess.setGuess('u');
		
		// Invoke 8 times then check response
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		gameService.evaluatePlayerGuess(playerGuess);
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals("Game is already complete", responseObj.getString("error"));
	}
	
	@Test
	public void testGameIdInvalid() {
		PlayerGuess playerGuess = new PlayerGuess();
		playerGuess.setGameId("abcdefg");
		
		String response = gameService.evaluatePlayerGuess(playerGuess);
		JsonObject responseObj = convertToObj(response);
		
		Assert.assertEquals("Invalid game id", responseObj.getString("error"));
	}
	
	private JsonObject convertToObj(String response) {
		InputStream inputStream = null;
		
		try {
			inputStream = IOUtils.toInputStream(response, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		JsonReader reader = Json.createReader(inputStream);
		return reader.readObject();
	}
	
}
