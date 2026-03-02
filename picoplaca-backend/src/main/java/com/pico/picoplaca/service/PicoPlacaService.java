package com.pico.picoplaca.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PicoPlacaService {

    public boolean canCirculate(String plate, LocalDateTime dateTime) {

        // Limpieza de placa
        plate = plate.trim();

        int lastDigit = Character.getNumericValue(
                plate.charAt(plate.length() - 1)
        );

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        LocalTime startMorning = LocalTime.of(7, 0);
        LocalTime endMorning = LocalTime.of(9, 30);

        LocalTime startEvening = LocalTime.of(16, 0);
        LocalTime endEvening = LocalTime.of(19, 30);

        boolean restrictedTime =
                (!time.isBefore(startMorning) && !time.isAfter(endMorning)) ||
                (!time.isBefore(startEvening) && !time.isAfter(endEvening));

        if (!restrictedTime) {
            return true;
        }

        switch (day) {
            case MONDAY:
                return !(lastDigit == 1 || lastDigit == 2);
            case TUESDAY:
                return !(lastDigit == 3 || lastDigit == 4);
            case WEDNESDAY:
                return !(lastDigit == 5 || lastDigit == 6);
            case THURSDAY:
                return !(lastDigit == 7 || lastDigit == 8);
            case FRIDAY:
                return !(lastDigit == 9 || lastDigit == 0);
            default:
                return true;
        }
    }
}