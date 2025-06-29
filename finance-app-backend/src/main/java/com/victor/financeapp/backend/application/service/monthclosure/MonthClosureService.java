package com.victor.financeapp.backend.application.service.monthclosure;

import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import reactor.core.publisher.Mono;

public interface MonthClosureService {

    Mono<MonthClosure> save(Balance balance);
}
