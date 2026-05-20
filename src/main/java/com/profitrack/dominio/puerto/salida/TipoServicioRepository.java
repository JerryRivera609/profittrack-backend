package com.profitrack.dominio.puerto.salida;

import com.profitrack.dominio.model.TipoServicio;

import java.util.List;
import java.util.Optional;

public interface TipoServicioRepository {
    TipoServicio guardar(TipoServicio tipoServicio);

    Optional<TipoServicio> buscarPorId(Long id);

    List<TipoServicio> buscarActivosPorEmpresa(Long empresaId);
}
