package com.pico.picoplaca;

import com.pico.picoplaca.exception.InvalidDateException;
import com.pico.picoplaca.exception.InvalidPlateException;
import com.pico.picoplaca.model.ValidationRequest;
import com.pico.picoplaca.model.ValidationResponse;
import com.pico.picoplaca.service.PicoPlacaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PicoPlacaServiceTest {

    private PicoPlacaService service;

    @BeforeEach
    void setUp() {
        service = new PicoPlacaService();
    }

    // ─── Validaciones de entrada ────────────────────────────────────────────

    @Test
    void shouldThrowWhenDateTimeIsNull() {
        ValidationRequest request = buildRequest("ABC-123", null);
        assertThrows(InvalidDateException.class, () -> service.validate(request));
    }

    @Test
    void shouldThrowWhenDateIsPast() {
        ValidationRequest request = buildRequest("ABC-123", LocalDateTime.now().minusDays(1));
        assertThrows(InvalidDateException.class, () -> service.validate(request));
    }

    @Test
    void shouldThrowWhenPlateIsNull() {
        ValidationRequest request = buildRequest(null, LocalDateTime.now().plusDays(1));
        assertThrows(InvalidPlateException.class, () -> service.validate(request));
    }

    @Test
    void shouldThrowWhenPlateFormatIsInvalid() {
        ValidationRequest request = buildRequest("1234AB", LocalDateTime.now().plusDays(1));
        assertThrows(InvalidPlateException.class, () -> service.validate(request));
    }

    // ─── Restricciones por día y dígito ─────────────────────────────────────

    @Test
    void shouldNotCirculateOnMondayMorningWithPlateEndingIn1() {
        // Lunes 07:30 → dígito 1 restringido
        LocalDateTime monday = nextDayOfWeek(1).withHour(7).withMinute(30);
        ValidationResponse response = service.validate(buildRequest("ABC-001", monday));
        assertFalse(response.isCanCirculate());
    }

    @Test
    void shouldNotCirculateOnMondayMorningWithPlateEndingIn2() {
        LocalDateTime monday = nextDayOfWeek(1).withHour(8).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-002", monday));
        assertFalse(response.isCanCirculate());
    }

    @Test
    void shouldNotCirculateOnTuesdayEveningWithPlateEndingIn3() {
        // Martes 17:00 → dígito 3 restringido
        LocalDateTime tuesday = nextDayOfWeek(2).withHour(17).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-003", tuesday));
        assertFalse(response.isCanCirculate());
    }

    @Test
    void shouldNotCirculateOnFridayWithPlateEndingIn0() {
        // Viernes 08:00 → dígito 0 restringido
        LocalDateTime friday = nextDayOfWeek(5).withHour(8).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-010", friday));
        assertFalse(response.isCanCirculate());
    }

    // ─── Casos donde SÍ puede circular ──────────────────────────────────────

    @Test
    void shouldCirculateOutsideRestrictedHours() {
        // Lunes 12:00 → fuera de horario, siempre puede circular
        LocalDateTime monday = nextDayOfWeek(1).withHour(12).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-001", monday));
        assertTrue(response.isCanCirculate());
    }

    @Test
    void shouldCirculateOnWeekend() {
        // Sábado 08:00 → sin restricción
        LocalDateTime saturday = nextDayOfWeek(6).withHour(8).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-123", saturday));
        assertTrue(response.isCanCirculate());
    }

    @Test
    void shouldCirculateWhenDigitDoesNotMatchDay() {
        // Lunes 08:00 → dígito 5 no está restringido en lunes
        LocalDateTime monday = nextDayOfWeek(1).withHour(8).withMinute(0);
        ValidationResponse response = service.validate(buildRequest("ABC-005", monday));
        assertTrue(response.isCanCirculate());
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private ValidationRequest buildRequest(String plate, LocalDateTime dateTime) {
        ValidationRequest request = new ValidationRequest();
        request.setPlate(plate);
        request.setDateTime(dateTime);
        return request;
    }

    /**
     * Retorna el próximo día de la semana a partir de hoy.
     * dayOfWeek: 1=Lunes, 2=Martes, ..., 7=Domingo
     */
    private LocalDateTime nextDayOfWeek(int dayOfWeek) {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        while (now.getDayOfWeek().getValue() != dayOfWeek) {
            now = now.plusDays(1);
        }
        return now;
    }
}
