package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.service.monthclosure.MonthClosureService;
import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.SaveMonthClosureUseCase;
import com.victor.financeapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveMonthClosureUseCaseImpl implements SaveMonthClosureUseCase {

    private final UserService userService;
    private final BalanceService balanceService;
    private final MonthClosureService monthClosureService;

    @Override
//    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void execute() {
        var yearMonth = YearMonth.now();

        userService.findUsersWithMonthClosureToday()
                .flatMap(user -> loadUserBalance(user, yearMonth)
                        .flatMap(userService::calculateUserBalance)
                        .flatMap(monthClosureService::execute)
                        .doOnSuccess(v -> log.info("Fechamento concluído para usuário {}", user.getId()))
                        .doOnError(e -> log.error("Erro no fechamento para usuário {}", user.getId(), e))
                        .then(), 4
                )
                .doOnSubscribe(s -> log.info("Iniciando job de fechamento mensal"))
                .doOnComplete(() -> log.info("Job de fechamento mensal finalizado"))
                .doOnError(e -> log.error("Erro geral no job de fechamento", e))
                .subscribe();
    }

    private Mono<User> loadUserBalance(User user, YearMonth yearMonth) {
        return this.balanceService.loadUserBalance(user, yearMonth)
                .map(balance -> {
                    user.setBalance(balance);
                    return user;
                });
    }
}
