package com.allblue.lookbook.application;

import com.allblue.lookbook.application.dto.result.ModelImageResult;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModelImageService {

    @Value("${app.image.model-path}")
    private String modelImagePath;

    @Value("${app.image.base-url}")
    private String imageBaseUrl;

    public List<ModelImageResult> getModelImages(TargetGender gender) {
        String subdir = resolveSubdir(gender);
        Path dir = Paths.get(modelImagePath, subdir);

        if (!Files.isDirectory(dir)) {
            log.warn("[ModelImageService] 디렉토리 없음: {}", dir);
            return Collections.emptyList();
        }

        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(p -> isImageFile(p.getFileName().toString()))
                    .sorted()
                    .map(p -> new ModelImageResult(imageBaseUrl + "/models/" + subdir + "/" + p.getFileName()))
                    .toList();
        } catch (IOException e) {
            log.error("[ModelImageService] 모델 이미지 목록 조회 실패: {}", dir, e);
            return Collections.emptyList();
        }
    }

    private String resolveSubdir(TargetGender gender) {
        if (gender == TargetGender.MEN) return "model-male";
        return "model-female";
    }

    private boolean isImageFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".webp");
    }
}
