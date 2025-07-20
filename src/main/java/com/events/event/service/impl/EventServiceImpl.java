package com.events.event.service.impl;

import com.events.event.dto.EventDto;
import com.events.event.entity.Event;
import com.events.event.mapper.EventMapper;
import com.events.event.repository.EventRepository;
import com.events.event.service.EventService;
import com.events.user.entity.User;
import com.events.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final IUserRepository userRepository;
    private final EventMapper mapper;

    @Override
    public EventDto createEvent(EventDto dto, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Event event = mapper.toEntity(dto);
        event.setOrganizer(organizer);
        event.setValidated(false); // en attente de validation
        return mapper.toDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(mapper::toDto).toList();
    }

    @Override
    public EventDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public List<EventDto> getEventsNearby(Double lat, Double lon, Double radius) {
        return eventRepository.findByLocationNear(lat, lon, radius).stream()
                .map(mapper::toDto).toList();
    }
}
