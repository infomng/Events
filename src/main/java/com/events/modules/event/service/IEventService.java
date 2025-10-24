package com.events.modules.event.service;

import com.events.modules.event.dto.CreateEventCommand;
import com.events.modules.event.dto.EventDto;
import com.events.modules.event.dto.UpdateEventCommand;

import java.util.List;

public interface IEventService {
    Long createEvent(CreateEventCommand command);
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    List<EventDto> getEventsNearby(Double lat, Double lon, Double radiusInMeters);
    void updateEvent(Long id, UpdateEventCommand command);
    List<EventDto> getAllIncomingEvents();
//TODO:void deleteEvent(Long id);
//TODO:List<EventDto> searchEvents(String keyword, String category, Double lat, Double lon, Double radiusInMeters);
//TODO:void addParticipant(Long eventId, Long userId);
//TODO:void removeParticipant(Long eventId, Long userId);
//TODO:List<Long> getParticipants(Long eventId);
//TODO:EventStatisticsDto getEventStatistics(Long eventId);
}