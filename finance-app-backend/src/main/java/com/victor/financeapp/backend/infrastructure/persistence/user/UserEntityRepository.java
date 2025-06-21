package com.victor.financeapp.backend.infrastructure.persistence.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
interface UserEntityRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findByEmail(String email);

    @Query("""
                SELECT u.* from finance_app.finance_app_user u
                INNER JOIN finance_app.user_property up on u.id = up.user_id
                WHERE up.property_name = 'MONTH_CLOSURE_DAY' AND up.property_value = (SELECT EXTRACT(DAY FROM now())::text)
            """)
    Flux<UserEntity> findUsersWithMonthClosureToday();
}
