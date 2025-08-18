package com.sparta.spartatigers.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LiveBoardCrawlerRunner implements ApplicationRunner {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void run(ApplicationArguments args)  {
        LocalDate targetDate = parseDate(args.getOptionValues("targetDate").get(0));
        log.info("Target date: {}", targetDate);
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
