package com.events.modules.event.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateEventCommandDto(@NotNull String name,
                                    @NotNull String description,
                                    @NotNull String location,
                                    @NotNull LocalDateTime startDate,
                                    @NotNull LocalDateTime endDate,
                                    @NotNull Integer totalTickets,
                                    @NotNull Boolean isFree,
                                    @NotNull Boolean isPublic,
                                    @NotNull Boolean isFreeEntry,
                                    Double latitude,
                                    Double longitude,
                                    LocalDateTime ticketSalesStartDate,
                                    LocalDateTime ticketSalesEndDate,
                                    Double ticketPrice) {
}