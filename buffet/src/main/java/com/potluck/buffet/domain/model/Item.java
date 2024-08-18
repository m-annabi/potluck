package com.potluck.buffet.domain.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Item(
        @Id String id,
        String name,
        Category category
) {
    public enum Category {
        MAIN_COURSE,
        DESERT,
        BREWERY
    }
}