package com.sparta.spartatigers.infrastructure.message;

import com.sparta.spartatigers.application.LiveBoardNotifier;
import com.sparta.spartatigers.domain.LiveBoardData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLiveBoardNotifier implements LiveBoardNotifier {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void notifyLiveBoardData(Long matchId, LiveBoardData liveBoardData) {
        String channel = String.format("live_board:match:%d", matchId);
        
        try {
            redisTemplate.convertAndSend(channel, liveBoardData);
            log.debug("Published live board data to channel: {}", channel);
        } catch (Exception e) {
            log.error("Failed to publish live board data to Redis channel: {}", channel, e);
        }
    }
}