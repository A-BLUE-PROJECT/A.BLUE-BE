package com.allblue.lookbook.application;

import com.allblue.lookbook.application.dto.command.LookbookCompleteCommand;
import com.allblue.lookbook.application.dto.command.LookbookCreateCommand;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LookbookCommandService {

    private final LookbookRepository lookbookRepository;

    public Long create(LookbookCreateCommand command) {
        Lookbook lookbook = Lookbook.create(
                command.styleType(),
                command.season(),
                command.targetGender(),
                command.tags(),
                command.items());
        return lookbookRepository.save(lookbook).getId();
    }

    public void complete(LookbookCompleteCommand command) {
        Lookbook lookbook = lookbookRepository.findById(command.lookbookId())
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.complete(command.originUrl(), command.imageUrl());
    }

    public void fail(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.fail();
    }

    public void delete(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbookRepository.delete(lookbook);
    }
}
