package com.events.event.service;

import com.events.event.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto dto, Long organizerId);
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    List<EventDto> getEventsNearby(Double lat, Double lon, Double radiusInMeters);
}
