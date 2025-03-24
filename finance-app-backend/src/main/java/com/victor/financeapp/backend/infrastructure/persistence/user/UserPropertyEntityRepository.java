package com.victor.financeapp.backend.infrastructure.persistence.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserPropertyEntityRepository extends ReactiveCrudRepository<UserPropertyEntity, Long> {
    Flux<UserPropertyEntity> findAllByUserId(Long userId);
}
