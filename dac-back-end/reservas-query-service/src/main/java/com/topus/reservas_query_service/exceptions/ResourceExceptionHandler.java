package com.topus.reservas_query_service.exceptions;


import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ReservaNaoEncontradaExeption.class)
    public ResponseEntity<StandarError> vooNaoEncontrado(ReservaNaoEncontradaExeption e, HttpServletRequest request) {

        StandarError error = StandarError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Reserva não foi encontrada")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UsuarioReservaNaoEncontrado.class)
    public ResponseEntity<StandarError> usuarioReservasNaoEncontrado(UsuarioReservaNaoEncontrado e,
            HttpServletRequest request) {

        StandarError error = StandarError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Reserva não encontrada")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
