package com.allblue.lookbook.infrastructure;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import com.allblue.lookbook.infrastructure.jpa.LookbookJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LookbookRepositoryImpl implements LookbookRepository {

    private final LookbookJpaRepository lookbookJpaRepository;

    @Override
    public Lookbook save(Lookbook lookbook) {
        return lookbookJpaRepository.save(lookbook);
    }

    @Override
    public Optional<Lookbook> findById(Long id) {
        return lookbookJpaRepository.findById(id);
    }

    @Override
    public List<Lookbook> findAll() {
        return lookbookJpaRepository.findAll();
    }

    @Override
    public List<Lookbook> findAllByStatus(LookbookStatus status) {
        return lookbookJpaRepository.findByStatus(status);
    }

    @Override
    public List<Lookbook> findApproved(Long cursorId, int size) {
        PageRequest pageable = PageRequest.of(0, size + 1);
        if (cursorId == null) {
            return lookbookJpaRepository.findByStatusOrderByIdDesc(LookbookStatus.APPROVED, pageable);
        }
        return lookbookJpaRepository.findByStatusAndIdLessThanOrderByIdDesc(LookbookStatus.APPROVED, cursorId, pageable);
    }

    @Override
    public List<Lookbook> findByStatusAndCreatedAtBefore(com.allblue.lookbook.domain.model.enums.LookbookStatus status, java.time.LocalDateTime threshold) {
        return lookbookJpaRepository.findByStatusAndCreatedAtBefore(status, threshold);
    }

    @Override
    public void delete(Lookbook lookbook) {
        lookbookJpaRepository.delete(lookbook);
    }
}
