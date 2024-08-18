package com.potluck.buffet.infrastructure.web;


import com.potluck.buffet.service.EventService;
import com.potluck.buffet.domain.model.Event;
import com.potluck.buffet.domain.model.Participation;
import com.potluck.buffet.domain.model.User;
import com.potluck.buffet.domain.repository.IEventRepository;
import com.potluck.buffet.domain.repository.IUserRepository;
import com.potluck.buffet.exceptions.EventNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*", "https://buffet-canadien-ui.herokuapp.com"}, maxAge = 3600)
// For Swagger
@BasePathAwareController
// Prod & Test
@RepositoryRestController

@Log4j2
public class EventController {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IEventRepository eventRepository;

    @Autowired
    EventService eventService;

    @ResponseBody
    @PostMapping("/event")
    public Event createEvent(@RequestBody Event newEvent) {
        return eventRepository.save(newEvent);
    }

    @PutMapping("/events/{id}/invite")
    @ResponseBody
    void invite(@PathVariable String id, @RequestParam String guestEmail) {
        eventService.inviteUserByEmail(id, guestEmail);

    }

    @PatchMapping(path = "/events/{eventId}/participations/{participantId}" , produces = "application/hal+json")
    @ResponseBody
    EntityModel<Event> updateParticipation(@PathVariable String eventId, @PathVariable String participantId, @RequestBody Participation newParticipation) {
        log.info("eventId = {}, participationId = {}", eventId, participantId);
        Event event = eventRepository.findById(eventId).get();
        List<Participation> currentParticipations = event.getParticipations();

        for(int i = 0 ; i < event.getParticipations().size() ; i++){
            if(null != participantId && participantId.equals(currentParticipations.get(i).getParticipant().getId())){
                if(null != newParticipation.getArrivalTime()){
                    currentParticipations.get(i).setArrivalTime(newParticipation.getArrivalTime());
                }
                if(0 != newParticipation.getNbGuests()){
                    currentParticipations.get(i).setNbGuests(newParticipation.getNbGuests());
                }
                if(null != newParticipation.getComments()){
                    currentParticipations.get(i).setComments(newParticipation.getComments());
                }
                if(null != newParticipation.getStatus()){
                    currentParticipations.get(i).setStatus(newParticipation.getStatus());
                }
                if(null != newParticipation.getDepartureTime()){
                    currentParticipations.get(i).setDepartureTime(newParticipation.getDepartureTime());
                }
                if(null != newParticipation.getParticipationItems()){
                    currentParticipations.get(i).setParticipationItems(newParticipation.getParticipationItems());
                }
            }
        }
        event.setParticipations(currentParticipations);
        event = eventRepository.save(event);
        return EntityModel.of(event);
    }

    @ResponseBody
    @PutMapping(path = "/events/{eventId}/participations/{participantId}", produces = "application/hal+json")
    EntityModel<Event> addParticipation(@PathVariable String eventId, @PathVariable String participantId,@RequestBody Participation newParticipation) {
        Event event = eventRepository.findById(eventId).get();
        List<Participation> currentParticipations = event.getParticipations();
        boolean existingParticipationfound = false;



        for(int i = 0 ; i < event.getParticipations().size() ; i++) {
            if (null != participantId && participantId.equals(currentParticipations.get(i).getParticipant().getId())) {
                User participant = currentParticipations.get(i).getParticipant();
                newParticipation.setParticipant(participant);
                currentParticipations.set(i, newParticipation);
                existingParticipationfound = true;
                log.debug("Replaced current participation for participant id {}", participantId);
            }
        }

        if (!existingParticipationfound){
            User participant = userRepository.findById(participantId).get();
            if (null != participantId){
                newParticipation.setParticipant(participant);
            } else{
                throw(new EventNotFoundException());
            }

            currentParticipations.add(newParticipation);
            log.debug("Added new participation for participant id {}", participantId);
        }
        event.setParticipations(currentParticipations);
        event = eventRepository.save(event);

        return EntityModel.of(event);
    }

    @ResponseBody
    @DeleteMapping(path = "/events/{eventId}/participations/{participantId}", produces = "application/hal+json")
    EntityModel<Event> deleteParticipation(@PathVariable String eventId, @PathVariable String participantId) {
        // TODO nettoyer
        Event event = eventRepository.findById(eventId).get();
        List<Participation> currentParticipations = event.getParticipations();
        boolean existingParticipationfound = false;
        int currentNumberOfParticipations = currentParticipations.size();
        Participation[] copy = new Participation[currentNumberOfParticipations];
        int newIndex = 0;
        for(int i = 0 ; i < currentNumberOfParticipations ; i++) {
            // for each participation which is not the one we want to remove, copy it
            String currentParticipantId = currentParticipations.get(i).getParticipant().getId();
            if (null != participantId && !participantId.equals(currentParticipantId)) {
                copy[newIndex] = currentParticipations.get(i);
                newIndex++;
            } else {
                currentParticipations.remove(i);
                existingParticipationfound = true;
                log.debug("Removing participation for participant id {}", participantId);
            }
        }

        if (!existingParticipationfound){
            throw(new EventNotFoundException());
        } else{
            event.setParticipations(currentParticipations);
            event = eventRepository.save(event);
        }
        return EntityModel.of(event);
    }
}
