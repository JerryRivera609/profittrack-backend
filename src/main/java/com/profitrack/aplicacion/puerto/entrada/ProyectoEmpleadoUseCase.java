package com.profitrack.aplicacion.puerto.entrada;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoRequestDto;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoResponseDto;
import java.util.List;
public interface ProyectoEmpleadoUseCase {
    ProyectoEmpleadoResponseDto asignar(ProyectoEmpleadoRequestDto dto);
    List<ProyectoEmpleadoResponseDto> listarPorProyecto(Long proyectoId);
    void remover(Long id);
}
