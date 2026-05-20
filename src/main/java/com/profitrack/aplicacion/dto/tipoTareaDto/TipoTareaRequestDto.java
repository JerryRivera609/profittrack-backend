package com.profitrack.aplicacion.dto.tipoTareaDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoTareaRequestDto {

    private Long empresaId;

    @NotBlank
    private String nombre;

    private String descripcion;
}
