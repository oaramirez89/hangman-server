package com.rvt.hangman.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rvt.hangman.server.model.PlayerGuess;
import com.rvt.hangman.server.service.GameService;

@RestController
public class HangmanController {
	
	@Autowired
	private GameService gameService;
	
	@PostMapping("/new")
	public String newGame() {
		return gameService.createGame();
	}
	
	@PostMapping("/guess")
	public String guess(@RequestBody PlayerGuess playerGuess) {
		return gameService.evaluatePlayerGuess(playerGuess);
	}

}
