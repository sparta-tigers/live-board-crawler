package com.sparta.spartatigers.infrastructure.repository;

import com.sparta.spartatigers.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMatchRepository extends JpaRepository<Match, Long>, MatchRepositoryCustom {
}
