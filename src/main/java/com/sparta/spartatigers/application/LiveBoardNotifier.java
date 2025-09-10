package com.sparta.spartatigers.application;

import com.sparta.spartatigers.domain.LiveBoardData;

public interface LiveBoardNotifier {
    void notifyLiveBoardData(Long matchId, LiveBoardData liveBoardData);
}
