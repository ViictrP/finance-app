package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.BudgetTemplate;
import com.victor.financeapp.backend.domain.repository.BudgetTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class BudgetTemplateRepositoryImpl implements BudgetTemplateRepository {

    private final BudgetTemplateEntityRepository templateRepository;
    private final BudgetTemplateCategoryEntityRepository categoryRepository;
    private final BudgetTemplateEntityMapper mapper;

    @Override
    @Transactional
    public Mono<BudgetTemplate> save(BudgetTemplate template) {
        var templateEntity = mapper.toEntity(template);

        Mono<BudgetTemplateEntity> savedTemplateEntityMono;
        if (templateEntity.getId() == null) {
            savedTemplateEntityMono = templateRepository.save(templateEntity);
        } else {
            savedTemplateEntityMono = Mono.just(templateEntity);
        }

        return savedTemplateEntityMono.flatMap(savedTemplateEntity -> {
            var categoryEntities = template.getCategories().stream()
                    .map(category -> {
                        var categoryEntity = mapper.toEntity(category);
                        categoryEntity.setTemplateId(savedTemplateEntity.getId());
                        return categoryEntity;
                    })
                    .toList();

            return categoryRepository.saveAll(categoryEntities)
                    .collectList()
                    .map(savedCategoryEntities -> {
                        var savedTemplate = mapper.toDomain(savedTemplateEntity);
                        savedTemplate.setCategories(
                                savedCategoryEntities.stream()
                                        .map(mapper::toDomain)
                                        .toList());
                        return savedTemplate;
                    });
        });
    }

    @Override
    public Mono<BudgetTemplate> findByUserId(Long userId) {
        return templateRepository.findByUserId(userId)
                .flatMap(templateEntity ->
                        categoryRepository.findByTemplateId(templateEntity.getId())
                                .map(mapper::toDomain)
                                .collectList()
                                .map(categories -> {
                                    var template = mapper.toDomain(templateEntity);
                                    template.setCategories(categories);
                                    return template;
                                })
                );
    }
}
