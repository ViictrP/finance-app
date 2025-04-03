package com.victor.financeapp.backend.infrastructure.persistence.user;

import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {

    private final UserEntityRepository repository;
    private final UserPropertyEntityRepository userPropertyEntityRepository;

    private final UserEntityMapper mapper;

    @Override
    public Mono<User> findUser(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain)
                .flatMap(this::loadUserProperties);
    }

    private Mono<User> loadUserProperties(User user) {
        return userPropertyEntityRepository.findAllByUserId(user.getId())
                .collectList()
                .map(properties -> {
                    properties.forEach(property -> user.addProperty(property.getPropertyName(), property.getPropertyValue()));
                    return user;
                });
    }
}
