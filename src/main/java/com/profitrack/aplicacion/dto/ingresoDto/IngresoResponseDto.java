package com.profitrack.aplicacion.dto.ingresoDto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data @Builder
public class IngresoResponseDto {
    private Long id;
    private Long empresaId;
    private Long proyectoId;
    private String proyectoNombre;
    private String tipo;
    private BigDecimal monto;
    private LocalDate fechaIngreso;
    private String descripcion;
    private Boolean activo;
}
