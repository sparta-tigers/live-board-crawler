package com.sparta.spartatigers.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartatigers.domain.QTeam;
import com.sparta.spartatigers.domain.MatchDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.spartatigers.domain.QMatch.match;
import static com.sparta.spartatigers.domain.QStadium.stadium;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryCustomImpl implements MatchRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchDetail> findMatchDetailsByDate(LocalDate targetDate) {
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.plusDays(1).atStartOfDay();

        QTeam awayTeam = new QTeam("awayTeam");
        QTeam homeTeam = new QTeam("homeTeam");

        return queryFactory
                .select(Projections.constructor(MatchDetail.class,
                        match.id.as("matchId"),
                        awayTeam.id.as("awayTeamId"),
                        awayTeam.name.as("awayTeamName"),
                        awayTeam.code.as("awayTeamCode"),
                        homeTeam.id.as("homeTeamId"),
                        homeTeam.name.as("homeTeamName"),
                        homeTeam.code.as("homeTeamCode"),
                        match.awayScore,
                        match.homeScore,
                        match.matchTime,
                        stadium.id.as("stadiumId"),
                        stadium.name.as("stadiumName")
                ))
                .from(match)
                .join(match.awayTeam, awayTeam)
                .join(match.homeTeam, homeTeam)
                .join(match.stadium, stadium)
                .where(
                        match.matchTime.gt(startOfDay)
                                .and(match.matchTime.lt(endOfDay))
                )
                .fetch();

    }
}
