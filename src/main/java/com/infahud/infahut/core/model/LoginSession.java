package com.infahud.infahut.core.model;

public record LoginSession(
    Long id, Long seq, String sessionId, String baseApiUrl, String response, String timestamp) {}
