package com.profitrack.aplicacion.puerto.entrada;

import com.profitrack.aplicacion.dto.rolDto.RolPatchDto;
import com.profitrack.aplicacion.dto.rolDto.RolRequestDto;
import com.profitrack.aplicacion.dto.rolDto.RolResponseDto;

import java.util.List;

public interface RolUseCase {
    RolResponseDto crear(RolRequestDto dto);
    RolResponseDto obtenerPorId(Long id, Long empresaId);
    List<RolResponseDto> listarActivos(Long empresaId);
    List<RolResponseDto> listarInactivos(Long empresaId);
    RolResponseDto actualizar(Long id, RolPatchDto dto, Long empresaId);
    RolResponseDto reactivar(Long id, Long empresaId);
    void eliminar(Long id, Long empresaId);
}
