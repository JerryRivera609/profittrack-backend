package com.profitrack.aplicacion.dto.proyectoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProyectoRequestDto {

    private Long empresaId;

    private Long clienteId;

    @NotNull
    private Long tipoServicioId;

    private Long liderEmpleadoId;

    private String codigo;

    @NotBlank
    private String nombre;

    private String descripcion;

    private LocalDate fechaInicioPlanificada;
    private LocalDate fechaFinPlanificada;

    private BigDecimal horasPlanificadas;
    private BigDecimal presupuestoPlanificado;
    private BigDecimal margenPlanificado;
    private BigDecimal precioVenta;
}
