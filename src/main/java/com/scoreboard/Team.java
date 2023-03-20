package com.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Team {
    private final String name;
    private int score;

    public Team(String name) {
        this.name = name;
        this.score = 0;
    }

    void updateScore(int score) {
        this.score = score;
    }
}
