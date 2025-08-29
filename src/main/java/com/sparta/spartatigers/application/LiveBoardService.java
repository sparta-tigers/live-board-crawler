package com.sparta.spartatigers.application;

import com.sparta.spartatigers.domain.LiveBoardUrls;
import com.sparta.spartatigers.domain.MatchDetail;
import com.sparta.spartatigers.domain.TeamCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LiveBoardService {
    private static final String BASE_URL = "https://www.koreabaseball.com/Game/LiveText.aspx";

    public LiveBoardUrls getLiveBoardUrls(List<MatchDetail> matchDetails) {
        return new LiveBoardUrls(matchDetails.stream()
                .map(matchDetail -> {
                    LocalDateTime matchTime = matchDetail.getMatchTime();
                    String yyyymmdd = matchTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    int year = matchTime.getYear();

                    TeamCode homeTeamCode = matchDetail.getHomeTeamCode();
                    TeamCode awayTeamCode = matchDetail.getAwayTeamCode();

                    String gameId = String.format("%s%s%s0", yyyymmdd, awayTeamCode.name(), homeTeamCode.name());

                    return String.format("%s?leagueId=1&seriesId=0&gameId=%s&gyear=%d", BASE_URL, gameId, year);
                })
                .toList());
    }
}
