package com.events.modules.auth.dto;

import lombok.Builder;

@Builder
public record AccessToken(String access_token) {
}
