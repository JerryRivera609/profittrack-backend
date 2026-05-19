package com.profitrack.aplicacion.dto.egresoDto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data @Builder
public class EgresoResponseDto {
    private Long id;
    private Long empresaId;
    private Long proyectoId;
    private String proyectoNombre;
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal monto;
    private LocalDate fechaEgreso;
    private String descripcion;
    private Boolean activo;
}
