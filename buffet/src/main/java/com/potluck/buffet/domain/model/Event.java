package com.potluck.buffet.domain.model;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    String id;

    @FutureOrPresent(message = "Event must be today or in the future")
    private Instant startTime;

    private Instant endTime;

    private EventStatus status;

    private EventCategory category;

    private String title;

    private String description;

    private int nbMaxGuests;

    Address location;

    @DocumentReference
    private User organizer;

    private List<Participation> participations = new ArrayList<>();


    public enum EventStatus{
        PENDING,
        PUBLISHED,
        CANCELLED,
        FINISHED
    }

    public enum EventCategory{
        BIRTHDAY,
        PRIVATE_PARTY
    }

    public void addParticipation (Participation participation){
        participation.setId(String.valueOf(UUID.randomUUID()));
        this.participations.add(participation);
    }

}