package com.potluck.buffet.domain.repository;

import com.potluck.buffet.domain.model.Event;
import com.potluck.buffet.domain.model.EventStaticData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface IEventRepository extends MongoRepository<Event, String> {

    Optional<Event> findByTitle(String title);

    @Query("{'organizer' : :#{#organizer}}")
    List<Event> findByOrganizer(@Param("organizer") ObjectId organizer);

    @Aggregation(pipeline = {
            "{ '$match': { 'participations.participant._id' : :#{#userParticipant} } }",
            "{ '$addFields': { 'participations': { " +
                    "$filter: { " +
                    "input: '$participations', " +
                    "as: 'participation', " +
                    "cond: { $eq: [ '$$participation.participant._id', { $toObjectId: :#{#userParticipant} } ] } } } } }"
    })
    List<Event> findByUserParticipant(@Param("userParticipant") ObjectId userParticipant);

    @Aggregation(pipeline = {
            "{ '$match': { '_id' : :#{#event} } }",
            "{ '$unwind': '$participations' }",
            "{ '$unwind': '$participations.participationItems' }",
            "{ '$project': { 'category': '$participations.participationItems.item.category', 'qty': '$participations.participationItems.qty' } }",
            "{ '$group': { '_id': '$category', 'totalQty': { '$sum': '$qty' } } }",
            "{ '$project': { '_id': 1, 'category': '$_id', 'totalQty': '$totalQty' } }"
    })
    List<EventStaticData> getStaticData(@Param("event") ObjectId event);

}

