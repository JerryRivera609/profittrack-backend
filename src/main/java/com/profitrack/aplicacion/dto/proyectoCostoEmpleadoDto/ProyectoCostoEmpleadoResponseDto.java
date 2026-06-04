package com.profitrack.aplicacion.dto.proyectoCostoEmpleadoDto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProyectoCostoEmpleadoResponseDto {
    private Long id;
    private Long proyectoId;
    private String proyectoNombre;
    private Long empleadoId;
    private String empleadoNombre;
    private BigDecimal costoHora;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean vigente;
    private Boolean activo;
}
