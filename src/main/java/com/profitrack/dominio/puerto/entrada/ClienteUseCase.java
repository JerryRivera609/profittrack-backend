package com.profitrack.dominio.puerto.entrada;

import com.profitrack.aplicacion.dto.clienteDto.ClientePatchDto;
import com.profitrack.aplicacion.dto.clienteDto.ClienteRequestDto;
import com.profitrack.aplicacion.dto.clienteDto.ClienteResponseDto;

import java.util.List;

public interface ClienteUseCase {
    ClienteResponseDto crear(ClienteRequestDto dto);
    ClienteResponseDto obtenerPorId(Long id);
    List<ClienteResponseDto> listarActivosPorEmpresa(Long empresaId);
    List<ClienteResponseDto> listarInactivosPorEmpresa(Long empresaId);
    ClienteResponseDto actualizar(Long id, ClientePatchDto dto);
    void eliminar(Long id);
    ClienteResponseDto reactivar(Long id);
}
