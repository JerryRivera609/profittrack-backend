package com.profitrack.dominio.puerto.salida;

import com.profitrack.dominio.model.EtapaProyecto;

import java.util.List;
import java.util.Optional;

public interface EtapaProyectoRepository {
    EtapaProyecto guardar(EtapaProyecto etapa);
    Optional<EtapaProyecto> buscarPorId(Long id);
    List<EtapaProyecto> buscarActivasPorProyecto(Long proyectoId);
    List<EtapaProyecto> buscarInactivasPorProyecto(Long proyectoId);
}
