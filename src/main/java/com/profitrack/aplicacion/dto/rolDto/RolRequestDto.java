package com.profitrack.aplicacion.dto.rolDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RolRequestDto {

    private Long empresaId;

    @NotBlank
    private String nombre;

    private String descripcion;
}
