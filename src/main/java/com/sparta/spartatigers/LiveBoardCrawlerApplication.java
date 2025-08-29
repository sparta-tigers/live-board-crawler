package com.sparta.spartatigers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class LiveBoardCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveBoardCrawlerApplication.class, args);
    }

}
