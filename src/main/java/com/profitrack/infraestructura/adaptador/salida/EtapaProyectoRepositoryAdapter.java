package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.EtapaProyecto;
import com.profitrack.dominio.puerto.salida.EtapaProyectoRepository;
import com.profitrack.infraestructura.repository.EtapaProyectoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EtapaProyectoRepositoryAdapter implements EtapaProyectoRepository {

    private final EtapaProyectoJpaRepository jpaRepository;

    @Override
    public EtapaProyecto guardar(EtapaProyecto etapa) {
        return jpaRepository.save(etapa);
    }

    @Override
    public Optional<EtapaProyecto> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<EtapaProyecto> buscarActivasPorProyecto(Long proyectoId) {
        return jpaRepository.findAllByProyectoIdAndActivoTrueOrderByOrdenAsc(proyectoId);
    }

    @Override
    public List<EtapaProyecto> buscarInactivasPorProyecto(Long proyectoId) {
        return jpaRepository.findAllByProyectoIdAndActivoFalseOrderByOrdenAsc(proyectoId);
    }
}
