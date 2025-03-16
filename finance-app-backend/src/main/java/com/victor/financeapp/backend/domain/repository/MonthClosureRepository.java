package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.MonthClosure;
import reactor.core.publisher.Flux;

public interface MonthClosureRepository {
    Flux<MonthClosure> findUsersLastFiveMonthClosures(Long userId);
}
