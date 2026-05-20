package com.profitrack.aplicacion.dto.historialCostoHoraDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HistorialCostoHoraRequestDto {
    @NotNull
    private Long empleadoId;
    
    @NotNull
    private BigDecimal costoHora;
    
    @NotNull
    private LocalDate fechaInicio;
    
    private LocalDate fechaFin;
}
