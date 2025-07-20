package com.events.event.controller;


import com.events.event.dto.EventDto;
import com.events.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping("/{organizerId}")
    public EventDto create(@RequestBody EventDto dto, @PathVariable Long organizerId) {
        return service.createEvent(dto, organizerId);
    }

    @GetMapping
    public List<EventDto> getAll() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @GetMapping("/nearby")
    public List<EventDto> getNearby(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "5000") Double radius // 5 km par défaut
    ) {
        return service.getEventsNearby(lat, lon, radius);
    }
}
