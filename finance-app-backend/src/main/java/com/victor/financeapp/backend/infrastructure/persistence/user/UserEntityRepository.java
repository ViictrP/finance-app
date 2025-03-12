package com.victor.financeapp.backend.infrastructure.persistence.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserEntityRepository extends ReactiveCrudRepository<UserEntity, Long> {
}
