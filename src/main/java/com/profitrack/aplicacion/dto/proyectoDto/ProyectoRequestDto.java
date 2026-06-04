package com.profitrack.aplicacion.dto.proyectoDto;

import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @Valid
    private List<EtapaProyectoRequestDto> etapas;
}
