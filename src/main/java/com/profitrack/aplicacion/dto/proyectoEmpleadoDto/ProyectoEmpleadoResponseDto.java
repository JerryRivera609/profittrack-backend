package com.profitrack.aplicacion.dto.proyectoEmpleadoDto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
@Data @Builder
public class ProyectoEmpleadoResponseDto {
    private Long id;
    private Long proyectoId;
    private String proyectoNombre;
    private Long empleadoId;
    private String empleadoNombre;
    private String rolAsignado;
    private Instant fechaAsignacion;
    private BigDecimal costoHoraCongelado;
    private Boolean activo;
}
