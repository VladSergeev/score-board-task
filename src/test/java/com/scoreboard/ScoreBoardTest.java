package com.scoreboard;

import com.scoreboard.exception.GameNotFoundException;
import com.scoreboard.exception.InvalidScoreException;
import com.scoreboard.request.CreateGameRequest;
import com.scoreboard.request.UpdateScoreRequest;
import com.scoreboard.response.GameSummary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


class ScoreBoardTest {

    Map<UUID, Game> testGames;
    ScoreBoard board;

    @BeforeEach
    void setup() {
        testGames = new HashMap<>();
        board = new ScoreBoard(testGames);
    }

    @Test
    void startGame() {
        UUID id = board.startGame(new CreateGameRequest("A", "B"));
        Game actual = testGames.get(id);
        assertThat(actual).isEqualToIgnoringGivenFields(new Game("A", "B"), "startTime");
        assertThat(actual.getStartTime()).isNotNull();
    }

    @Test
    void updateScore() {

        UUID id = UUID.randomUUID();

        testGames.put(id, Game.builder()
                .homeTeam(Team.builder()
                        .name("A")
                        .score(13)
                        .build())
                .awayTeam(Team.builder()
                        .name("B")
                        .score(45)
                        .build())
                .build());


        board.updateScore(new UpdateScoreRequest(id, 43, 12));

        assertThat(testGames.get(id))
                .isEqualTo(Game.builder()
                        .homeTeam(Team.builder()
                                .name("A")
                                .score(43)
                                .build())
                        .awayTeam(Team.builder()
                                .name("B")
                                .score(12)
                                .build())
                        .build());

    }

    @Test
    void with_not_existed_game_updateScore() {
        UUID id = UUID.randomUUID();
        Throwable throwable = catchThrowable(() -> board.updateScore(new UpdateScoreRequest(id, 43, 12)));
        assertThat(throwable).isExactlyInstanceOf(GameNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"0,-1", "1,-1", "-1,0", "-1,1"})
    void with_invalid_score_updateScore(int homeScore, int awayScore) {
        UUID id = UUID.randomUUID();
        Throwable throwable = catchThrowable(() -> board.updateScore(new UpdateScoreRequest(id, homeScore, awayScore)));
        assertThat(throwable).isExactlyInstanceOf(InvalidScoreException.class);
    }

    @Test
    void finishGame() {

        UUID id = UUID.randomUUID();

        testGames.put(id, Game.builder()
                .homeTeam(Team.builder()
                        .name("A")
                        .score(13)
                        .build())
                .awayTeam(Team.builder()
                        .name("B")
                        .score(45)
                        .build())
                .build());

        board.finishGame(id);
        assertThat(testGames.size()).isEqualTo(0);

    }

    @Test
    void with_no_game_finishGame() {
        UUID id = UUID.randomUUID();

        testGames.put(id, Game.builder()
                .homeTeam(Team.builder()
                        .name("A")
                        .score(13)
                        .build())
                .awayTeam(Team.builder()
                        .name("B")
                        .score(45)
                        .build())
                .build());

        board.finishGame(UUID.randomUUID());
        assertThat(testGames).isEqualTo(Map.of(id, Game.builder()
                .homeTeam(Team.builder()
                        .name("A")
                        .score(13)
                        .build())
                .awayTeam(Team.builder()
                        .name("B")
                        .score(45)
                        .build())
                .build()));
    }

    @Test
    void with_no_games_inProgressSummary() {
        assertThat(board.inProgressSummary()).isEqualTo(List.of());
    }

    @Test
    void with_games_sorted_by_total_score_inProgressSummary() {
        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A3")
                        .score(0)
                        .build())
                .awayTeam(Team.builder()
                        .name("B3")
                        .score(1)
                        .build())
                .build());

        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A1")
                        .score(1)
                        .build())
                .awayTeam(Team.builder()
                        .name("B1")
                        .score(2)
                        .build())
                .build());

        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A2")
                        .score(1)
                        .build())
                .awayTeam(Team.builder()
                        .name("B2")
                        .score(1)
                        .build())
                .build());


        assertThat(board.inProgressSummary())
                .isEqualTo(List.of(
                        new GameSummary("A1", 1, "B1", 2),
                        new GameSummary("A2", 1, "B2", 1),
                        new GameSummary("A3", 0, "B3", 1)
                ));
    }


    @Test
    void with_games_sorted_by_date_desc_inProgressSummary() {
        LocalDateTime now = LocalDateTime.now();
        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A3")
                        .score(3)
                        .build())
                .awayTeam(Team.builder()
                        .name("B3")
                        .score(1)
                        .build())
                .startTime(now.plusMinutes(1))
                .build());

        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A1")
                        .score(2)
                        .build())
                .awayTeam(Team.builder()
                        .name("B1")
                        .score(2)
                        .build())
                .startTime(now.minusMinutes(1))

                .build());

        testGames.put(UUID.randomUUID(), Game.builder()
                .homeTeam(Team.builder()
                        .name("A2")
                        .score(1)
                        .build())
                .awayTeam(Team.builder()
                        .name("B2")
                        .score(3)
                        .build())
                .startTime(now.plusSeconds(12))
                .build());


        assertThat(board.inProgressSummary())
                .isEqualTo(List.of(
                        new GameSummary("A3", 3, "B3", 1),
                        new GameSummary("A2", 1, "B2", 3),
                        new GameSummary("A1", 2, "B1", 2)
                ));
    }


}