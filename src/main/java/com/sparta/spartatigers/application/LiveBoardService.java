package com.sparta.spartatigers.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.sparta.spartatigers.domain.LiveBoardData;
import com.sparta.spartatigers.domain.LiveBoardUrls;
import com.sparta.spartatigers.domain.MatchDetail;
import com.sparta.spartatigers.domain.TeamCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LiveBoardService {
    private static final String BASE_URL = "https://www.koreabaseball.com/Game/LiveText.aspx";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);


    public void startLiveBoardCrawler(List<MatchDetail> matchDetails) {
        LiveBoardUrls liveBoardUrls = getLiveBoardUrls(matchDetails);

        Playwright playwright = null;
        Browser browser = null;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            playwright = Playwright.create();
            browser = playwright.chromium().launch();
            Browser finalBrowser = browser;
            liveBoardUrls.getTargetUrls().forEach(url ->
                    scheduler.scheduleWithFixedDelay(() -> crawlLiveBoard(url, finalBrowser),
                            0,
                            5,
                            TimeUnit.SECONDS));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutdown hook triggered");
                shutdownLatch.countDown();
            }));

            log.info("LiveBoard crawler started.");
            shutdownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Application interrupted");
        } finally {
            scheduler.shutdown();
            log.info("shceduler shutdown");
            if (playwright != null) playwright.close();
            log.info("playwright close");
        }
    }

    public void crawlLiveBoard(String url, Browser browser) {
        try {
            Page page = browser.newPage();
            page.navigate(url);
            // 네트워크 멈출 때 까지 대기해야 제대로 응답 가지고옴
            page.waitForLoadState(LoadState.NETWORKIDLE);

            String script = """
                    (() => {
                        const players = [];
                        const playerList = document.querySelectorAll('.playerName ul li');
                    
                        playerList.forEach(li => {
                            const role = li.className;
                            const name = li.innerText.trim();
                            players.push({role, name});
                        });
                    
                        const strike = document.querySelectorAll('.sbo .s li.on').length;
                        const ball = document.querySelectorAll('.sbo .b li.on').length;
                        const out = document.querySelectorAll('.sbo .o li.on').length;
                        const homeScoreEm = document.querySelectorAll('.teamHome em');
                        const awayScoreEm = document.querySelectorAll('.teamAway em');
                        const homeScore = homeScoreEm.length > 0 ? homeScoreEm[0].innerText.trim() : '0';
                        const awayScore = awayScoreEm.length > 0 ? awayScoreEm[0].innerText.trim() : '0';
                        const pitcherCountElem = document.querySelectorAll('.pitcher .today span');
                        const pitcherCount = pitcherCountElem.length > 0 ? pitcherCountElem[0].innerText.trim().split('투구')[0] : '0';
                        const currentInningElem = document.querySelectorAll('.base strong');
                        const currentInning = currentInningElem.length > 0 ? currentInningElem[0].innerText.trim() : '1회';
                    
                        const inningOneTexts = [...document.querySelectorAll('#numCont1 span')].map(span => span.innerText.trim());
                        const inningTwoTexts = [...document.querySelectorAll('#numCont2 span')].map(span => span.innerText.trim());
                        const inningThreeTexts = [...document.querySelectorAll('#numCont3 span')].map(span => span.innerText.trim());
                        const inningFourTexts = [...document.querySelectorAll('#numCont4 span')].map(span => span.innerText.trim());
                        const inningFiveTexts = [...document.querySelectorAll('#numCont5 span')].map(span => span.innerText.trim());
                        const inningSixTexts = [...document.querySelectorAll('#numCont6 span')].map(span => span.innerText.trim());
                        const inningSevenTexts = [...document.querySelectorAll('#numCont7 span')].map(span => span.innerText.trim());
                        const inningEightTexts = [...document.querySelectorAll('#numCont8 span')].map(span => span.innerText.trim());
                        const inningNineTexts = [...document.querySelectorAll('#numCont9 span')].map(span => span.innerText.trim());
                        const inningExtraTexts = [...document.querySelectorAll('#numCont10 span')].map(span => span.innerText.trim());
                    
                        const inningTexts = {
                            inningOneTexts, inningTwoTexts, inningThreeTexts, inningFourTexts, inningFiveTexts,
                            inningSixTexts, inningSevenTexts, inningEightTexts, inningNineTexts, inningExtraTexts
                        };
                    
                        const matchScore = {strike, ball, out, homeScore, awayScore, pitcherCount};
                    
                        return {players, matchScore, inningTexts, currentInning};
                    })();
                    """;

            Object result = page.evaluate(script);
            log.info(objectMapper.writeValueAsString(result));
            LiveBoardData liveBoardData = convertToLiveBoardData(result);
            log.info("{} 크롤링 완료", url);

            // TODO Pub to Redis

            page.close();
        } catch (Exception e) {
            log.error("LiveBoardCrawler error", e);
            throw new RuntimeException(e);
        }
    }

    private LiveBoardData convertToLiveBoardData(Object result) {
        try {
            return objectMapper.convertValue(result, LiveBoardData.class);
        } catch (Exception e) {
            log.error("LiveBoardCrawler error", e);
            throw new RuntimeException(e);
        }
    }

    private LiveBoardUrls getLiveBoardUrls(List<MatchDetail> matchDetails) {
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
