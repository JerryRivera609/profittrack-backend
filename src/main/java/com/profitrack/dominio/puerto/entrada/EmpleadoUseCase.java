package com.profitrack.dominio.puerto.entrada;

import com.profitrack.aplicacion.dto.EmpleadoPatchDto;
import com.profitrack.aplicacion.dto.EmpleadoRequestDto;
import com.profitrack.aplicacion.dto.EmpleadoResponseDto;

import java.util.List;

public interface EmpleadoUseCase {
    EmpleadoResponseDto crear(EmpleadoRequestDto dto);
    EmpleadoResponseDto obtenerPorId(Long id);
    List<EmpleadoResponseDto> listarActivosPorEmpresa(Long empresaId);
    EmpleadoResponseDto actualizar(Long id, EmpleadoPatchDto dto);
    void eliminar(Long id);
}
