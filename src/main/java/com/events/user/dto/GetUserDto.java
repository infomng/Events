package com.events.user.dto;

public record GetUserDto (
    String id,
    String email,
    String fullName,
    boolean isVerified
) {
}