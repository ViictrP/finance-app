package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import com.victor.financeapp.backend.domain.model.MonthClosure;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class MonthClosureRepositoryImpl implements MonthClosureRepository {

    private final MonthClosureEntityRepository repository;
    private final MonthClosureEntityMapper mapper;

    @Override
    public Flux<MonthClosure> findUsersLastFiveMonthClosures(Long userId) {
        return repository.findAll()
                .map(mapper::toDomain);
    }
}
