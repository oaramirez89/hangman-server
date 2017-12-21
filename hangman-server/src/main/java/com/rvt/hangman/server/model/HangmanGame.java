package com.rvt.hangman.server.model;

import javax.json.Json;
import javax.json.JsonObject;

public class HangmanGame {
	
	private String gameId = "";
	private String wordToGuess = "";
	private String playerGuesses = "";
	private int incorrect = 0;
	private GameStatus status = null;
	
	public String getGameId() {
		return gameId;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public String getWordToGuess() {
		return wordToGuess;
	}
	
	public void setWordToGuess(String word) {
		this.wordToGuess = word;
	}
	
	public String getPlayerGuesses() {
		return playerGuesses;
	}
	
	public void setPlayerGuesses(String playerGuesses) {
		this.playerGuesses = playerGuesses;
	}
	
	public int getIncorrect() {
		return incorrect;
	}
	
	public void setIncorrect(int incorrect) {
		this.incorrect = incorrect;
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public JsonObject newFormat() {
		return Json.createObjectBuilder()
			     .add("gameId", this.getGameId())
			     .add("word", this.getPlayerGuesses())
			     .build();
	}
	
	public JsonObject gameStatus() {
		return Json.createObjectBuilder()
			     .add("gameId", this.getGameId())
			     .add("word", this.getPlayerGuesses())
			     .add("incorrect", this.getIncorrect())
			     .add("status", this.getStatus().toString())
			     .build();
	}
}
