package com.profitrack.aplicacion.puerto.entrada;

import com.profitrack.aplicacion.dto.proyectoCostoEmpleadoDto.ProyectoCostoEmpleadoResponseDto;

import java.util.List;

public interface ProyectoCostoEmpleadoUseCase {
    List<ProyectoCostoEmpleadoResponseDto> listarPorProyecto(Long proyectoId);
}
