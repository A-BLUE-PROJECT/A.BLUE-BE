package com.allblue.activelog.application.dto.command;

import com.allblue.activelog.domain.model.SwipeType;

public record SwipeCommand(Long userId, Long lookbookId, SwipeType swipeType) {}
