package com.profitrack.aplicacion.dto.registroHorasDto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RegistroHorasResumenDto {
    private BigDecimal totalHorasRegistradas;
    private BigDecimal totalHorasAprobadas;
    private BigDecimal totalHorasPendientes;
    private BigDecimal totalHorasRechazadas;
    private List<HorasPorProyectoDto> horasPorProyecto;
    private List<HorasPorEmpleadoDto> horasPorEmpleado;

    @Data
    @Builder
    public static class HorasPorProyectoDto {
        private Long proyectoId;
        private String proyectoNombre;
        private BigDecimal horas;
    }

    @Data
    @Builder
    public static class HorasPorEmpleadoDto {
        private Long empleadoId;
        private String empleadoNombre;
        private BigDecimal horas;
    }
}
