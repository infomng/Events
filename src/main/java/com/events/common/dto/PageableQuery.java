package com.events.common.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageableQuery {
    @Min(0)
    private Integer page;

    @Min(1)
    private Integer size;

    private String sort;
    private String direction;
}
