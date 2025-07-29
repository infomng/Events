package com.events.event.service;

import com.events.event.dto.CreateEventCommand;
import com.events.event.dto.EventDto;
import com.events.event.dto.UpdateEventCommand;

import java.util.List;

public interface IEventService {
    Long createEvent(CreateEventCommand command);
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    List<EventDto> getEventsNearby(Double lat, Double lon, Double radiusInMeters);
    void updateEvent(Long id, UpdateEventCommand command);
}
