package com.profitrack.dominio.puerto.entrada;

import com.profitrack.aplicacion.dto.TipoServicioPatchDto;
import com.profitrack.aplicacion.dto.TipoServicioRequestDto;
import com.profitrack.aplicacion.dto.TipoServicioResponseDto;

import java.util.List;

public interface TipoServicioUseCase {
    TipoServicioResponseDto crear(TipoServicioRequestDto dto);
    TipoServicioResponseDto obtenerPorId(Long id);
    List<TipoServicioResponseDto> listarActivosPorEmpresa(Long empresaId);
    TipoServicioResponseDto actualizar(Long id, TipoServicioPatchDto dto);
    void eliminar(Long id);
}
