package com.potluck.buffet.domain.model;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation implements Serializable {
    @Id String id;

    User participant;

    @PositiveOrZero
    private int nbGuests;

    private Instant arrivalTime;

    private Instant departureTime;

    private String comments;

    private List<ParticipationItem> participationItems;

    private ParticipationStatus status;

    public enum ParticipationStatus {
        INVITED,
        NOT_SURE,
        WILL_ATTEND,
        WILL_NOT_ATTEND

    }

    public void addItem (ParticipationItem participationItem){
        participationItem.setId(String.valueOf(UUID.randomUUID()));
        this.participationItems.add(participationItem);
    }
}
