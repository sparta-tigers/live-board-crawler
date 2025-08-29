package com.sparta.spartatigers.infrastructure.repository;

import com.sparta.spartatigers.domain.MatchDetail;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepositoryCustom {
    List<MatchDetail> findMatchDetailsByDate(LocalDate date);
}

