package com.profitrack.aplicacion.dto.proyectoDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProyectoPatchDto {
    private Long clienteId;
    private Long tipoServicioId;
    private Long liderEmpleadoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicioPlanificada;
    private LocalDate fechaFinPlanificada;
    private LocalDate fechaInicioReal;
    private LocalDate fechaFinReal;
    private BigDecimal horasPlanificadas;
    private BigDecimal presupuestoPlanificado;
    private BigDecimal margenPlanificado;
    private BigDecimal precioVenta;
    private String estado;
}
