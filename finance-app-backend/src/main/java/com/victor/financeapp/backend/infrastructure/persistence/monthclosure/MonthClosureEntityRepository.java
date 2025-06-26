package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface MonthClosureEntityRepository extends ReactiveCrudRepository<MonthClosureEntity, Long> {

    @Query("""
        SELECT * FROM finance_app.month_closure WHERE user_id = :userId
        ORDER BY
            year DESC,
            index DESC
        LIMIT 5;
    """)
    Flux<MonthClosureEntity> findLastFiveMonthClosures(Long userId);
}
