package com.sparta.spartatigers.application;

import com.sparta.spartatigers.domain.MatchRepository;
import com.sparta.spartatigers.domain.MatchDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public List<MatchDetail> getMatchDetails(LocalDate targetDate) {
        return matchRepository.findMatchDetailsByDate(targetDate);
    }
}
