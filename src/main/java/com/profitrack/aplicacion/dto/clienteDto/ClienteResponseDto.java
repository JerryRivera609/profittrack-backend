package com.profitrack.aplicacion.dto.clienteDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponseDto {
    private Long id;
    private Long empresaId;
    private String razonSocial;
    private String ruc;
    private String nombreContacto;
    private String correoContacto;
    private String telefonoContacto;
    private String direccion;
    private Boolean activo;
}
