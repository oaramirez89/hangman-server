package com.rvt.hangman.server.model;

public class PlayerGuess {

	private String gameId = "";
	private char guess = ' ';
	
	public String getGameId() {
		return gameId;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public char getGuess() {
		return guess;
	}
	
	public void setGuess(char guess) {
		this.guess = guess;
	}
}
