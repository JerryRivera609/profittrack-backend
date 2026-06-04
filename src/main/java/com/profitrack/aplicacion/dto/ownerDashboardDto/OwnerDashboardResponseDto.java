package com.profitrack.aplicacion.dto.ownerDashboardDto;

import com.profitrack.aplicacion.dto.egresoDto.EgresoResponseDto;
import com.profitrack.aplicacion.dto.ingresoDto.IngresoResponseDto;
import com.profitrack.aplicacion.dto.metricaDto.MetricaSnapshotResponseDto;
import com.profitrack.aplicacion.dto.metricaDto.RentabilidadResponseDto;
import com.profitrack.aplicacion.dto.proyectoCostoEmpleadoDto.ProyectoCostoEmpleadoResponseDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoResponseDto;
import com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResumenDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OwnerDashboardResponseDto {
    private ProyectoResponseDto proyecto;
    private RentabilidadResponseDto rentabilidad;
    private RegistroHorasResumenDto resumenHoras;
    private List<CostoEmpleadoDto> costosPorEmpleado;
    private List<ProyectoEmpleadoResponseDto> equipo;
    private List<ProyectoCostoEmpleadoResponseDto> costosAplicados;
    private List<IngresoResponseDto> ingresos;
    private List<EgresoResponseDto> egresos;
    private List<MetricaSnapshotResponseDto> snapshots;
    private String semaforo;
    private List<String> alertas;

    @Data
    @Builder
    public static class CostoEmpleadoDto {
        private Long empleadoId;
        private String empleadoNombre;
        private BigDecimal totalHoras;
        private BigDecimal totalCosto;
        private Integer registros;
    }
}
