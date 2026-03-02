package com.pico.picoplaca.controller;

import com.pico.picoplaca.model.ValidationRequest;
import com.pico.picoplaca.model.ValidationResponse;
import com.pico.picoplaca.service.PicoPlacaService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PicoPlacaController {

    private final PicoPlacaService service;

    public PicoPlacaController(PicoPlacaService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ValidationResponse validate(@RequestBody ValidationRequest request) {

        if (request.getDateTime() == null) {
            throw new RuntimeException("Fecha inválida");
        }

        if (request.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha no puede ser pasada");
        }

        boolean result = service.canCirculate(
                request.getPlate(),
                request.getDateTime()
        );

        String message = result
                ? "El vehículo puede circular en la fecha indicada."
                : "El vehículo NO puede circular en la fecha indicada.";

        return new ValidationResponse(
                request.getPlate(),
                result,
                message
        );
    }
}