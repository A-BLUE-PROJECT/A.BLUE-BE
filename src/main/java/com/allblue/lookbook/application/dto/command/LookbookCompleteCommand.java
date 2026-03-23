package com.allblue.lookbook.application.dto.command;

public record LookbookCompleteCommand(
        Long lookbookId,
        String originUrl,
        String imageUrl) {}
