package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.mapper.UserMapper;
import com.victor.financeapp.backend.application.service.user.UserDomainService;
import com.victor.financeapp.backend.application.usercase.GetBalanceUseCase;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.infrastructure.security.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserBalanceUseCaseImpl implements GetBalanceUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    private final UserMapper userMapper;

    @Override
    public Mono<UserBalanceDTO> execute(YearMonth yearMonth) {
        return SecurityContext.getUserEmail()
                .flatMap(email -> loadUser(yearMonth, email));
    }

    private Mono<UserBalanceDTO> loadUser(YearMonth yearMonth, String userEmail) {
        return userRepository.findUser(userEmail)
                .flatMap(user -> this.userDomainService.loadUserData(user, yearMonth))
                .flatMap(userDomainService::calculateUserBalance)
                .map(userMapper::toBalanceDTO);
    }
}
