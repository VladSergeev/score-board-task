package com.scoreboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {


    @Test
    void updateScore() {

        Team team = Team.builder()
                .name("My team")
                .score(32)
                .build();
        team.updateScore(4);
        assertEquals(team, Team.builder()
                .name("My team")
                .score(4)
                .build());
    }

}