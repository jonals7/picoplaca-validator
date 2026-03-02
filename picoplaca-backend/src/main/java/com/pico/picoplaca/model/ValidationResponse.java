package com.pico.picoplaca.model;

public class ValidationResponse {

    private String plate;
    private boolean canCirculate;
    private String message;

    public ValidationResponse(String plate, boolean canCirculate, String message) {
        this.plate = plate;
        this.canCirculate = canCirculate;
        this.message = message;
    }

    public String getPlate() {
        return plate;
    }

    public boolean isCanCirculate() {
        return canCirculate;
    }

    public String getMessage() {
        return message;
    }
}