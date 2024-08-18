package com.potluck.buffet.domain.model;

import org.springframework.data.annotation.Id;
import java.io.Serializable;

public record EventStaticData(
        @Id String id,
        String category,
        Integer totalQty
) implements Serializable {}
