package com.profitrack.aplicacion.dto.egresoDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class EgresoRequestDto {
    private Long empresaId;
    private Long proyectoId;
    private Long categoriaId;
    @NotNull private BigDecimal monto;
    private LocalDate fechaEgreso;
    private String descripcion;
}
