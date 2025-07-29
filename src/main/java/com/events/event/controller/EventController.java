package com.events.event.controller;


import com.events.common.result.Result;
import com.events.event.dto.CreateEventCommand;
import com.events.event.dto.EventDto;
import com.events.event.dto.UpdateEventCommand;
import com.events.event.service.IEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final IEventService service;

    @PostMapping()
    public ResponseEntity<Result<Long>> create(@Valid @RequestBody CreateEventCommand command) {
        return ResponseEntity.ok(Result.success(service.createEvent(command)));
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

    @GetMapping("/{id}/update")
    public ResponseEntity<Result<Void>> updateEvent(@PathVariable Long id,@Valid @RequestBody UpdateEventCommand command) {
        service.updateEvent(id, command);
        return ResponseEntity.ok(Result.success());
    }
}
