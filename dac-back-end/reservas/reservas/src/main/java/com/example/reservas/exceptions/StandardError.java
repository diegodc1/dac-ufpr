package com.example.reservas.exceptions;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {
    private Instant timestamp;
    private Integer estado;
    private String error;
    private String message;
    private String path;
}
