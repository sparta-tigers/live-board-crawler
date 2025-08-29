package com.sparta.spartatigers.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class LiveBoardUrls {
    private List<String> targetUrls;

    public LiveBoardUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
    }
}
// https://www.koreabaseball.com/Game/LiveText.aspx?leagueId=1&seriesId=0&gameId=20250801SKOB0&gyear=2025
// https://www.koreabaseball.com/Game/LiveText.aspx?leagueId=1&seriesId=0&gameId=20250801SKOB0&year=2025