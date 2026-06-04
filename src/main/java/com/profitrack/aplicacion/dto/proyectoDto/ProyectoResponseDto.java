package com.profitrack.aplicacion.dto.proyectoDto;

import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoResponseDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProyectoResponseDto {
    private Long id;
    private Long empresaId;
    private Long clienteId;
    private String clienteNombre;
    private Long tipoServicioId;
    private String tipoServicioNombre;
    private Long liderEmpleadoId;
    private String liderNombre;
    private String codigo;
    private String nombre;
    private String descripcion;

    private LocalDate fechaInicioPlanificada;
    private LocalDate fechaFinPlanificada;
    private LocalDate fechaInicioReal;
    private LocalDate fechaFinReal;

    private BigDecimal horasPlanificadas;
    private BigDecimal horasReales;

    private BigDecimal presupuestoPlanificado;
    private BigDecimal costoReal;

    private BigDecimal margenPlanificado;
    private BigDecimal margenReal;

    private BigDecimal precioVenta;

    private String estado;
    private Boolean activo;
    private List<EtapaProyectoResponseDto> etapas;

    private String miRolEnProyecto;
    private Boolean soyLiderDelProyecto;
    private List<String> misPermisos;
}
