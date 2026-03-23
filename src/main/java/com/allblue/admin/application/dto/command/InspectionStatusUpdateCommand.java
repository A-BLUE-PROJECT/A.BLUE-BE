package com.allblue.admin.application.dto.command;

import com.allblue.admin.domain.model.InspectionStatus;

public record InspectionStatusUpdateCommand(Long inspectionId, InspectionStatus status, Long adminId) {}
