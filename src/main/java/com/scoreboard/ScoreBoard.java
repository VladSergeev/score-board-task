package com.scoreboard;

import com.scoreboard.exception.GameNotFoundException;
import com.scoreboard.exception.InvalidScoreException;
import com.scoreboard.request.CreateGameRequest;
import com.scoreboard.request.UpdateScoreRequest;
import com.scoreboard.response.GameSummary;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ScoreBoard {
    private final Map<UUID, Game> games;

    public ScoreBoard() {
        this(new ConcurrentHashMap<>());
    }

    ScoreBoard(Map<UUID, Game> games) {
        this.games = games;
    }


    public UUID startGame(CreateGameRequest request) {
        UUID uuid = UUID.randomUUID();
        games.put(uuid, new Game(request.homeTeamName(), request.awayTeamName()));
        return uuid;
    }

    public void updateScore(UpdateScoreRequest request) {

        validateScore(request::homeTeamScore);
        validateScore(request::awayTeamScore);

        Game game = games.get(request.id());
        if (game == null) {
            throw new GameNotFoundException();
        }
        game.updateScores(request.homeTeamScore(), request.awayTeamScore());
    }



    public void finishGame(UUID id) {
        games.remove(id);
    }


    public List<GameSummary> inProgressSummary() {
        return games.values()
                .stream()
                .sorted(ScoreBoard::compare)
                .map(game -> new GameSummary(game.getHomeTeam().getName(),
                        game.getHomeTeam().getScore(),
                        game.getAwayTeam().getName(),
                        game.getAwayTeam().getScore()))
                .toList();
    }

    private static int compare(Game first, Game second) {
        int result = second.totalScore() - first.totalScore();
        if (result != 0) {
            return result;
        }
        return second.getStartTime().compareTo(first.getStartTime());
    }

    private void validateScore(Supplier<Integer> scoreSupplier) {
        Integer score = scoreSupplier.get();
        if (score < 0) {
            throw new InvalidScoreException(score);
        }
    }

}
