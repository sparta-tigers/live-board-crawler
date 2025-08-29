package com.sparta.spartatigers.runner;

import com.sparta.spartatigers.application.LiveBoardService;
import com.sparta.spartatigers.application.MatchService;
import com.sparta.spartatigers.domain.LiveBoardUrls;
import com.sparta.spartatigers.domain.MatchDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveBoardCrawlerRunner implements ApplicationRunner {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final MatchService matchService;
    private final LiveBoardService liveBoardService;

    @Override
    public void run(ApplicationArguments args)  {
        LocalDate targetDate = parseDate(args.getOptionValues("targetDate").get(0));
        log.info("Target date: {}", targetDate);
        List<MatchDetail> matchDetails = matchService.getMatchDetails(targetDate);
        LiveBoardUrls liveBoardUrls = liveBoardService.getLiveBoardUrls(matchDetails);
        log.info("LiveBoardUrls: {}", liveBoardUrls);
    }

    private LocalDate parseDate(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Date string is null");
        }

        try {
            LocalDate date = LocalDate.parse(str, ISO_DATE_FORMATTER);
            return date.atStartOfDay(KST_ZONE).toLocalDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Date string is invalid");
        }
    }
}
