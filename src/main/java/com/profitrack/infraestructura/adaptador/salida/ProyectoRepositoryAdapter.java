package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.Proyecto;
import com.profitrack.dominio.puerto.salida.ProyectoRepository;
import com.profitrack.infraestructura.repository.ProyectoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProyectoRepositoryAdapter implements ProyectoRepository {

    private final ProyectoJpaRepository jpaRepository;

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return jpaRepository.save(proyecto);
    }

    @Override
    public Optional<Proyecto> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Proyecto> buscarActivosPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaIdAndActivoTrue(empresaId);
    }
}
