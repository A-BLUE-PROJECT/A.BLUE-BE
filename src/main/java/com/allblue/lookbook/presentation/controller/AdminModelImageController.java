package com.allblue.lookbook.presentation.controller;

import com.allblue.common.response.ApiResponse;
import com.allblue.lookbook.application.ModelImageService;
import com.allblue.lookbook.application.dto.result.ModelImageResult;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import com.allblue.lookbook.presentation.response.LookbookResultCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/models")
@RequiredArgsConstructor
public class AdminModelImageController implements AdminModelImageApi {

    private final ModelImageService modelImageService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<ModelImageResult>>> getModelImages(
            @RequestParam(required = false) TargetGender gender) {
        List<ModelImageResult> results = modelImageService.getModelImages(
                gender != null ? gender : TargetGender.WOMEN);
        return ResponseEntity.ok(ApiResponse.of(LookbookResultCode.MODEL_LIST_OK, results));
    }
}
