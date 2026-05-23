package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.MetricaProyecto;
import com.profitrack.dominio.puerto.salida.MetricaProyectoRepository;
import com.profitrack.infraestructura.repository.MetricaProyectoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricaProyectoRepositoryAdapter implements MetricaProyectoRepository {
    private final MetricaProyectoJpaRepository jpa;

    @Override
    public MetricaProyecto guardar(MetricaProyecto m) {
        return jpa.save(m);
    }

    @Override
    public List<MetricaProyecto> buscarPorProyecto(Long proyectoId) {
        return jpa.findAllByProyectoId(proyectoId);
    }
}
