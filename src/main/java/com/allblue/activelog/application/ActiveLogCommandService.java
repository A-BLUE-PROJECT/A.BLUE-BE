package com.allblue.activelog.application;

import com.allblue.activelog.application.dto.command.SwipeCommand;
import com.allblue.activelog.domain.model.ActiveLog;
import com.allblue.activelog.domain.repository.ActiveLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActiveLogCommandService {

    private final ActiveLogRepository activeLogRepository;

    public void saveSwipeAction(SwipeCommand command) {
        if (activeLogRepository.existsByUserIdAndLookbookId(command.userId(), command.lookbookId())) {
            return;
        }

        ActiveLog activeLog = ActiveLog.create(command.userId(), command.lookbookId(), command.swipeType());
        activeLogRepository.save(activeLog);
    }
}
