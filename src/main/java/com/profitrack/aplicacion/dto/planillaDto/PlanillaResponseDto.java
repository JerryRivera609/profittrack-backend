package com.profitrack.aplicacion.dto.planillaDto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
@Data @Builder
public class PlanillaResponseDto {
    private Long id;
    private Long empresaId;
    private Integer anio;
    private Integer mes;
    private BigDecimal montoTotal;
    private Boolean activo;
    private List<DetalleDto> detalles;
    @Data @Builder
    public static class DetalleDto {
        private Long id;
        private Long empleadoId;
        private String empleadoNombre;
        private BigDecimal sueldoBase;
        private BigDecimal bonos;
        private BigDecimal descuentos;
        private BigDecimal sueldoFinal;
    }
}
