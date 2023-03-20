package com.scoreboard.response;

public record GameSummary(String homeTeam,int homeTeamScore,
                          String awayTeam,int awayTeamScore) {
}
