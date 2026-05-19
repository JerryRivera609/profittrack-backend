package com.profitrack.aplicacion.dto.ingresoDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class IngresoRequestDto {
    private Long empresaId;
    private Long proyectoId;
    private String tipo;
    @NotNull private BigDecimal monto;
    private LocalDate fechaIngreso;
    private String descripcion;
}
