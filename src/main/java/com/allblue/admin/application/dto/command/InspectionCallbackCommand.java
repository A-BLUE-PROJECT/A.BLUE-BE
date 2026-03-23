package com.allblue.admin.application.dto.command;

import com.allblue.admin.domain.model.InspectionStatus;

public record InspectionCallbackCommand(Long lookbookImageId, InspectionStatus status, String aiComment) {}
