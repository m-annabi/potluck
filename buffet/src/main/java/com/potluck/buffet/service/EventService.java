package com.potluck.buffet.service;


import com.potluck.buffet.domain.model.Event;
import com.potluck.buffet.domain.model.Participation;
import com.potluck.buffet.domain.model.User;
import com.potluck.buffet.domain.repository.IEventRepository;
import com.potluck.buffet.domain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService{
    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public void inviteUserByEmail(String eventId, String guestEmail){
        Event event = eventRepository.findById(eventId).get();
        User guest = userRepository.findByEmail(guestEmail).get();
        Participation newParticipation = Participation.builder()
                .participant(guest)
                .status(Participation.ParticipationStatus.INVITED)
                .build();
        event.addParticipation(newParticipation);
        eventRepository.save(event);

        //guest.getParticipations().add(newParticipation);
        userRepository.save(guest);

    }
}
