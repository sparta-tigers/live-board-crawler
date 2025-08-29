package com.sparta.spartatigers.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchDetail {
    private Long matchId;
    private Long awayTeamId;
    private String awayTeamName;
    private TeamCode awayTeamCode;
    private Long homeTeamId;
    private String homeTeamName;
    private TeamCode homeTeamCode;
    private Integer awayScore;
    private Integer homeScore;
    private LocalDateTime matchTime;
    private Long stadiumId;
    private String stadiumName;
}
