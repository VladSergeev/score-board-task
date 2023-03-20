package com.scoreboard.request;

import java.util.UUID;

public record UpdateScoreRequest(UUID id, int homeTeamScore, int awayTeamScore) {
}
