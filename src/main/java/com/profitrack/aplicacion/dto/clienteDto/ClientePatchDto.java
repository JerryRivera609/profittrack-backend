package com.profitrack.aplicacion.dto.clienteDto;

import lombok.Data;

@Data
public class ClientePatchDto {
    private String razonSocial;
    private String ruc;
    private String nombreContacto;
    private String correoContacto;
    private String telefonoContacto;
    private String direccion;
}
