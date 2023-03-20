package com.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Game {
    private final Team homeTeam;
    private final Team awayTeam;
    private final LocalDateTime startTime;

    public Game(String homeTeamName, String awayTeamName) {
        this(new Team(homeTeamName), new Team(awayTeamName), LocalDateTime.now());
    }

    public void updateScores(int homeScore, int awayScore) {
        homeTeam.updateScore(homeScore);
        awayTeam.updateScore(awayScore);
    }

    public int totalScore() {
        return homeTeam.getScore() + awayTeam.getScore();
    }
}
