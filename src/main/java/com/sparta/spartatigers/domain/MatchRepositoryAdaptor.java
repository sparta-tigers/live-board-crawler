package com.sparta.spartatigers.domain;

import com.sparta.spartatigers.infrastructure.repository.JpaMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryAdaptor implements MatchRepository {

    private final JpaMatchRepository jpaMatchRepository;

    @Override
    public List<MatchDetail> findMatchDetailsByDate(LocalDate date) {
        return jpaMatchRepository.findMatchDetailsByDate(date);
    }
}
