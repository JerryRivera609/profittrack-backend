package com.profitrack.dominio.puerto.entrada;
import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoRequestDto;
import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoResponseDto;
import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoPatchDto;
import java.util.List;
public interface TareaProyectoUseCase {
    TareaProyectoResponseDto crear(TareaProyectoRequestDto dto);
    List<TareaProyectoResponseDto> listarPorProyecto(Long proyectoId);
    TareaProyectoResponseDto actualizar(Long id, TareaProyectoPatchDto dto);
    void eliminar(Long id);
}
