package com.scoreboard.exception;

public class InvalidScoreException extends RuntimeException {
    private static final String MESSAGE = "Invalid score - %d";

    public InvalidScoreException(int score) {
        super(String.format(MESSAGE, score));
    }
}
