package com.allblue.lookbook.domain.repository;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LookbookRepository {
    Lookbook save(Lookbook lookbook);
    Optional<Lookbook> findById(Long id);
    List<Lookbook> findAll();
    List<Lookbook> findApproved(Long cursorId, int size);
    List<Lookbook> findByStatusAndCreatedAtBefore(LookbookStatus status, LocalDateTime threshold);
    void delete(Lookbook lookbook);
}
