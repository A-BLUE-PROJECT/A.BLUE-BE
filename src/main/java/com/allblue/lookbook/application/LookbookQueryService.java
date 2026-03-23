package com.allblue.lookbook.application;

import com.allblue.lookbook.application.dto.query.LookbookSearchQuery;
import com.allblue.lookbook.application.dto.result.LookbookDetailResult;
import com.allblue.lookbook.application.dto.result.LookbookResult;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LookbookQueryService {

    private final LookbookRepository lookbookRepository;

    public List<LookbookResult> findAll(LookbookSearchQuery query) {
        return lookbookRepository.findAll().stream()
                .map(LookbookResult::from)
                .toList();
    }

    public LookbookDetailResult findById(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        return LookbookDetailResult.from(lookbook);
    }
}
