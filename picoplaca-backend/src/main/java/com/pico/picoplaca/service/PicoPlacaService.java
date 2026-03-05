package com.pico.picoplaca.service;

import com.pico.picoplaca.exception.InvalidDateException;
import com.pico.picoplaca.exception.InvalidPlateException;
import com.pico.picoplaca.model.ValidationRequest;
import com.pico.picoplaca.model.ValidationResponse;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PicoPlacaService {

    // Formato válido: 3 letras, guión opcional, 3 o 4 dígitos (ej: ABC-123 o ABC1234)
    private static final String PLATE_REGEX = "^[A-Za-z]{3}-?\\d{3,4}$";

    private static final LocalTime START_MORNING = LocalTime.of(7, 0);
    private static final LocalTime END_MORNING   = LocalTime.of(9, 30);
    private static final LocalTime START_EVENING = LocalTime.of(16, 0);
    private static final LocalTime END_EVENING   = LocalTime.of(19, 30);

    public ValidationResponse validate(ValidationRequest request) {

        // 1. Validar que la fecha no sea nula
        if (request.getDateTime() == null) {
            throw new InvalidDateException("La fecha y hora son obligatorias");
        }

        // 2. Validar que la fecha no sea pasada
        if (request.getDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("La fecha ingresada no puede ser anterior a la fecha actual");
        }

        // 3. Validar formato de placa
        String plate = request.getPlate() == null ? "" : request.getPlate().trim();
        if (!plate.matches(PLATE_REGEX)) {
            throw new InvalidPlateException(
                "Formato de placa inválido. Debe tener 3 letras seguidas de 3 o 4 dígitos (ej: ABC-123)"
            );
        }

        // 4. Evaluar restricción
        boolean canCirculate = canCirculate(plate, request.getDateTime());

        String message = canCirculate
                ? "El vehículo puede circular en la fecha y hora indicadas."
                : "El vehículo NO puede circular en la fecha y hora indicadas (restricción Pico y Placa).";

        return new ValidationResponse(plate.toUpperCase(), request.getDateTime(), canCirculate, message);
    }

    private boolean canCirculate(String plate, LocalDateTime dateTime) {

        int lastDigit = Character.getNumericValue(plate.charAt(plate.length() - 1));

        DayOfWeek day  = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        boolean restrictedTime =
                (!time.isBefore(START_MORNING) && !time.isAfter(END_MORNING)) ||
                (!time.isBefore(START_EVENING) && !time.isAfter(END_EVENING));

        if (!restrictedTime) {
            return true;
        }

        switch (day) {
            case MONDAY:    return !(lastDigit == 1 || lastDigit == 2);
            case TUESDAY:   return !(lastDigit == 3 || lastDigit == 4);
            case WEDNESDAY: return !(lastDigit == 5 || lastDigit == 6);
            case THURSDAY:  return !(lastDigit == 7 || lastDigit == 8);
            case FRIDAY:    return !(lastDigit == 9 || lastDigit == 0);
            default:        return true; // Sábado y domingo sin restricción
        }
    }
}