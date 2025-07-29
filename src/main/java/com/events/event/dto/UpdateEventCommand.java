package com.events.event.dto;

import com.events.event.enumeration.EventStatusEnum;

import java.time.LocalDateTime;

public record UpdateEventCommand(
        String name,
        String description,
        String location,
        Double latitude,
        Double longitude,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime ticketSalesStartDate,
        LocalDateTime ticketSalesEndDate,
        Integer totalTickets,
        Double ticketPrice,
        EventStatusEnum status
) {}

