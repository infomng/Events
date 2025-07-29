package com.events.event.service.impl;

import com.events.auth.service.auth.IAuthService;
import com.events.common.exception.BadRequestException;
import com.events.event.dto.CreateEventCommand;
import com.events.event.dto.EventDto;
import com.events.event.dto.UpdateEventCommand;
import com.events.event.entity.Event;
import com.events.event.enumeration.EventStatusEnum;
import com.events.event.exception.EventForbidenException;
import com.events.event.exception.EventNotFoundException;
import com.events.event.mapper.EventMapper;
import com.events.event.repository.EventRepository;
import com.events.event.service.IEventService;
import com.events.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final IAuthService authService;
    private final EventMapper mapper;

    @Override
    public Long createEvent(CreateEventCommand command) {
        if (command.startDate().isAfter(command.endDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        if (command.ticketSalesStartDate().isAfter(command.ticketSalesEndDate())) {
            throw new BadRequestException("Ticket sales start date cannot be after end date");
        }

        User currentUser = authService.getCurrentUser();

        Event event = Event.builder()
                .name(command.name())
                .description(command.description())
                .location(command.location())
                .startDate(command.startDate())
                .ticketSalesEndDate(command.ticketSalesStartDate())
                .ticketSalesStartDate(command.ticketSalesEndDate())
                .organizer(currentUser)
                .status(EventStatusEnum.DRAFT)
                .build();

        eventRepository.save(event);

        return event.getId();
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

    @Override
    public void updateEvent(Long id,UpdateEventCommand command) {
        User currentUser = authService.getCurrentUser();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new EventForbidenException(event.getId());
        }

        updateEvent(command, event);
    }

    private static void updateEvent(UpdateEventCommand command, Event event) {
        if(command.totalTickets() < event.getAvailableTickets()) {
            throw new BadRequestException("Total tickets cannot be less than available tickets");
        }

        if (command.startDate().isAfter(command.endDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        if (command.ticketSalesStartDate().isAfter(command.ticketSalesEndDate())) {
            throw new BadRequestException("Ticket sales start date cannot be after end date");
        }

        event.setName(command.name());
        event.setDescription(command.description());
        event.setLocation(command.location());
        event.setStartDate(command.startDate());
        event.setTicketSalesStartDate(command.ticketSalesStartDate());
        event.setTicketSalesEndDate(command.ticketSalesEndDate());
        event.setStatus(command.status());
        event.setLatitude(command.latitude());
        event.setLongitude(command.longitude());
        event.setTotalTickets(command.totalTickets());
        event.setPrice(command.ticketPrice());
        event.setAvailableTickets(command.totalTickets());
    }
}


