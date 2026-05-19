package com.profitrack.aplicacion.dto.proyectoEmpleadoDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class ProyectoEmpleadoRequestDto {
    @NotNull private Long proyectoId;
    @NotNull private Long empleadoId;
    private String rolAsignado;
}
