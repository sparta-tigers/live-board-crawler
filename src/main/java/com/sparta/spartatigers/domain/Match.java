package com.sparta.spartatigers.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "matches")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Column
    private LocalDateTime matchTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MatchResult matchResult;

    @Column
    private Integer homeScore;

    @Column
    private Integer awayScore;

    @Column
    private String remark; // 비고

    @Column
    private LocalDateTime reservationOpenTime;
}