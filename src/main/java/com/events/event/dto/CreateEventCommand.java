package com.events.event.dto;

import java.time.LocalDateTime;

public record CreateEventCommand(String name,
                                 String description,
                                 String location,
                                 Double latitude,
                                 Double longitude,
                                 LocalDateTime startDate,
                                 LocalDateTime endDate,
                                 LocalDateTime ticketSalesStartDate,
                                 LocalDateTime ticketSalesEndDate,
                                 Integer totalTickets,
                                 Double ticketPrice) {
}
