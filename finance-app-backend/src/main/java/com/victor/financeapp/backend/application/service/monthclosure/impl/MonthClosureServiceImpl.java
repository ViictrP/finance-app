package com.victor.financeapp.backend.application.service.monthclosure.impl;

import com.victor.financeapp.backend.application.service.monthclosure.MonthClosureService;
import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MonthClosureServiceImpl implements MonthClosureService {

    private final MonthClosureRepository monthClosureRepository;

    @Override
    public Mono<MonthClosure> execute(Balance balance) {
        return monthClosureRepository.save(balance.getMonthClosure());
    }
}
