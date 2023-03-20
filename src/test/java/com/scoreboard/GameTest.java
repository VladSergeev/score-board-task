package com.scoreboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    void updateScores(){
        Game game = new Game("A", "B");
        game.updateScores(21,23);
        assertEquals(game.getHomeTeam().getScore(),21);
        assertEquals(game.getAwayTeam().getScore(),23);
    }

    @Test
    void totalScore(){
        Game game = new Game("A", "B");
        game.updateScores(21,23);
        assertEquals(game.totalScore(),44);
    }

}