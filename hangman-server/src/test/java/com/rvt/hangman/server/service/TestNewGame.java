package com.rvt.hangman.server.service;

import org.junit.Assert;
import org.junit.Test;

public class TestNewGame {
	
	@Test
	public void testNewGameHasGameId() {
		GameService gameService = new GameService();
		
		String newGame = gameService.createGame();
		Assert.assertTrue(newGame.contains("gameId"));
	}
	
	@Test
	public void testNewGameHasWord() {
		GameService gameService = new GameService();
		
		String newGame = gameService.createGame();
		Assert.assertTrue(newGame.contains("word"));
	}
	
	@Test
	public void testNewGameHasAtLeastOneUnderscore() {
		GameService gameService = new GameService();
		
		String newGame = gameService.createGame();
		Assert.assertTrue(newGame.contains("_"));
	}
}
