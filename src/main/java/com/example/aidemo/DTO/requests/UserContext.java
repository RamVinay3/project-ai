package com.example.aidemo.DTO.requests;

public record UserContext(
        String userId,
        String name,
        String email,
        String timezone,
        String preferredLanguage,
        String attitude,
        String defaultMeetingDuration
) {}
