package com.pico.picoplaca.controller;

import com.pico.picoplaca.model.ValidationRequest;
import com.pico.picoplaca.model.ValidationResponse;
import com.pico.picoplaca.service.PicoPlacaService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PicoPlacaController {

    private final PicoPlacaService service;

    public PicoPlacaController(PicoPlacaService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validate(@RequestBody ValidationRequest request) {

        ValidationResponse response = service.validate(request);
        return ResponseEntity.ok(response);
    }
}