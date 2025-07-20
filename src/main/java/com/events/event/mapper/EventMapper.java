package com.events.event.mapper;


import com.events.event.entity.Event;
import com.events.event.dto.EventDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto toDto(Event event);
    Event toEntity(EventDto dto);
}