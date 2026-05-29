package com.profitrack.aplicacion.dto.etapaProyectoDto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EtapaProyectoPatchDto {
    private String nombre;
    private String descripcion;
    private Integer orden;

    @DecimalMin(value = "0.00")
    private BigDecimal horasPlanificadas;

    private LocalDate fechaInicioPlanificada;
    private LocalDate fechaFinPlanificada;
    private LocalDate fechaInicioReal;
    private LocalDate fechaFinReal;
    private String estado;
}
