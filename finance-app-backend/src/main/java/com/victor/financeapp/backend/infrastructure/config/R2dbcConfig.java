package com.victor.financeapp.backend.infrastructure.config;

import com.victor.financeapp.backend.domain.model.common.Category;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {
        // This is configured by Spring Boot auto-configuration
        return null;
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new CategoryReadConverter());
        converters.add(new CategoryWriteConverter());
        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }

    static class CategoryReadConverter implements Converter<String, Category> {
        @Override
        public Category convert(String source) {
            return Category.valueOf(source.toUpperCase());
        }
    }

    static class CategoryWriteConverter implements Converter<Category, String> {
        @Override
        public String convert(Category source) {
            return source.name();
        }
    }
}
