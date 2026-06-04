package com.profitrack.aplicacion.dto.empresaDashboardDto;

import com.profitrack.aplicacion.dto.egresoDto.EgresoResponseDto;
import com.profitrack.aplicacion.dto.ingresoDto.IngresoResponseDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class EmpresaDashboardFinancieroResponseDto {
    private Long empresaId;
    private String empresaNombre;
    private LocalDate fechaConsulta;
    private BigDecimal totalIngresoPlanificado;
    private BigDecimal totalIngresoReal;
    private BigDecimal totalCostoPlanificado;
    private BigDecimal totalCostoLaboral;
    private BigDecimal totalEgresoReal;
    private BigDecimal totalCostoReal;
    private BigDecimal margenPlanificado;
    private BigDecimal margenReal;
    private BigDecimal porcentajeMargen;
    private BigDecimal horasPlanificadas;
    private BigDecimal horasReales;
    private BigDecimal cpi;
    private BigDecimal spi;
    private Integer totalProyectos;
    private Integer proyectosRentables;
    private Integer proyectosEnRiesgo;
    private String semaforo;
    private List<String> alertas;
    private List<ProyectoFinancieroDto> proyectos;
    private List<IngresoResponseDto> ingresos;
    private List<EgresoResponseDto> egresos;

    @Data
    @Builder
    public static class ProyectoFinancieroDto {
        private Long proyectoId;
        private String proyectoNombre;
        private String estado;
        private String semaforo;
        private BigDecimal ingresoPlanificado;
        private BigDecimal ingresoReal;
        private BigDecimal costoPlanificado;
        private BigDecimal costoLaboral;
        private BigDecimal costoOpex;
        private BigDecimal costoReal;
        private BigDecimal margenPlanificado;
        private BigDecimal margenReal;
        private BigDecimal porcentajeMargen;
        private BigDecimal horasPlanificadas;
        private BigDecimal horasReales;
        private BigDecimal cpi;
        private BigDecimal spi;
        private Boolean esRentable;
    }
}
