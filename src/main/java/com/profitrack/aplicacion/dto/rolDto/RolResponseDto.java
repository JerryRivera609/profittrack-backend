package com.profitrack.aplicacion.dto.rolDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponseDto {
    private Long id;
    private Long empresaId;
    private String nombreEmpresa;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
