package com.pico.picoplaca.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ValidationResponse {

    private String plate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    private boolean canCirculate;
    private String message;

    public ValidationResponse(String plate, LocalDateTime dateTime, boolean canCirculate, String message) {
        this.plate        = plate;
        this.dateTime     = dateTime;
        this.canCirculate = canCirculate;
        this.message      = message;
    }

    public String getPlate() {
        return plate;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isCanCirculate() {
        return canCirculate;
    }

    public String getMessage() {
        return message;
    }
}