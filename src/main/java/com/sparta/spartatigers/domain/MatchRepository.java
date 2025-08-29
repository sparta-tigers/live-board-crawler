package com.sparta.spartatigers.domain;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository {
    List<MatchDetail> findMatchDetailsByDate(LocalDate date);
}
