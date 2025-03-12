package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MonthClosureEntityRepository extends ReactiveCrudRepository<MonthClosureEntity, Long> {
}
