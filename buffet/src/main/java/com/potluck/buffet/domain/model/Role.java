package com.potluck.buffet.domain.model;

import jakarta.persistence.Id;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document("roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@EqualsAndHashCode
public class Role {
    @Id
    String id;
    private String role;

}
