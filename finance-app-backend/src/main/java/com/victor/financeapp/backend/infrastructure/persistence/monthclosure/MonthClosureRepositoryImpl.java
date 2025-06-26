package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class MonthClosureRepositoryImpl implements MonthClosureRepository {

    private final MonthClosureEntityRepository repository;
    private final MonthClosureEntityMapper mapper;

    @Override
    public Flux<MonthClosure> findUsersLastFiveMonthClosures(Long userId) {
        return repository.findLastFiveMonthClosures(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<MonthClosure> save(MonthClosure monthClosure) {
        return repository.save(mapper.toEntity(monthClosure))
                .map(mapper::toDomain);
    }
}
