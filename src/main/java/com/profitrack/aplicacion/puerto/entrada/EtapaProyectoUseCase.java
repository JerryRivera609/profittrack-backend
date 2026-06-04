package com.profitrack.aplicacion.puerto.entrada;

import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoPatchDto;
import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoRequestDto;
import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoResponseDto;

import java.util.List;

public interface EtapaProyectoUseCase {
    EtapaProyectoResponseDto crear(EtapaProyectoRequestDto dto);
    EtapaProyectoResponseDto obtenerPorId(Long id);
    List<EtapaProyectoResponseDto> listarPorProyecto(Long proyectoId);
    List<EtapaProyectoResponseDto> listarInactivasPorProyecto(Long proyectoId);
    EtapaProyectoResponseDto actualizar(Long id, EtapaProyectoPatchDto dto);
    void eliminar(Long id);
    EtapaProyectoResponseDto reactivar(Long id);
}
