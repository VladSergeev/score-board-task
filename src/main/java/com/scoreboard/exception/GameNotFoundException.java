package com.scoreboard.exception;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException() {
        super("Game is not found!");
    }
}
