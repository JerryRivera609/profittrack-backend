package com.profitrack.aplicacion.dto.historialCostoHoraDto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class HistorialCostoHoraResponseDto {
    private Long id;
    private Long empleadoId;
    private String empleadoNombre;
    private BigDecimal costoHora;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
