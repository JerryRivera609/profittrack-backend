package com.profitrack.aplicacion.puerto.entrada;

import java.util.List;

import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaPatchDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaRequestDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaResponseDto;

public interface TipoTareaUseCase {
    TipoTareaResponseDto crear(TipoTareaRequestDto dto);

    TipoTareaResponseDto obtenerPorId(Long id);

    List<TipoTareaResponseDto> listarActivosPorEmpresa(Long empresaId);

    TipoTareaResponseDto actualizar(Long id, TipoTareaPatchDto dto);

    void eliminar(Long id);
}
