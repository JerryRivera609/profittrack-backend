package com.profitrack.aplicacion.dto.planillaDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
@Data
public class PlanillaRequestDto {
    private Long empresaId;
    @NotNull private Integer anio;
    @NotNull private Integer mes;
    private List<DetallePlanillaDto> detalles;
    @Data
    public static class DetallePlanillaDto {
        @NotNull private Long empleadoId;
        private BigDecimal sueldoBase;
        private BigDecimal bonos;
        private BigDecimal descuentos;
    }
}
