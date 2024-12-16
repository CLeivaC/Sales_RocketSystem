package com.rocketsystem.coreapi.rocketsytem_sales_api.dtos;

import java.time.LocalDateTime;

public class SuccessResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public SuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
