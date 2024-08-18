package com.potluck.buffet.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ParticipationItem {
    @Id String id;
    Item item;
    int qty;
}
