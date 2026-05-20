package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.TipoTarea;
import com.profitrack.dominio.puerto.salida.TipoTareaRepository;
import com.profitrack.infraestructura.repository.TipoTareaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoTareaRepositoryAdapter implements TipoTareaRepository {

    private final TipoTareaJpaRepository jpaRepository;

    @Override
    public TipoTarea guardar(TipoTarea t) {
        return jpaRepository.save(t);
    }

    @Override
    public Optional<TipoTarea> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<TipoTarea> buscarPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaId(empresaId);
    }

    @Override
    public List<TipoTarea> buscarActivosPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaIdAndActivoTrue(empresaId);
    }
}
