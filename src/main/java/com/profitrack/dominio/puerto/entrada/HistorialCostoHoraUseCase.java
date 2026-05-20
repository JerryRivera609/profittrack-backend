package com.profitrack.dominio.puerto.entrada;

import com.profitrack.aplicacion.dto.historialCostoHoraDto.HistorialCostoHoraRequestDto;
import com.profitrack.aplicacion.dto.historialCostoHoraDto.HistorialCostoHoraResponseDto;

import java.util.List;

public interface HistorialCostoHoraUseCase {
    HistorialCostoHoraResponseDto registrarCosto(HistorialCostoHoraRequestDto dto);
    List<HistorialCostoHoraResponseDto> listarPorEmpleado(Long empleadoId);
}
