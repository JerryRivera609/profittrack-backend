package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.TipoServicio;
import com.profitrack.dominio.puerto.salida.TipoServicioRepository;
import com.profitrack.infraestructura.repository.TipoServicioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoServicioRepositoryAdapter implements TipoServicioRepository {

    private final TipoServicioJpaRepository jpaRepository;

    @Override
    public TipoServicio guardar(TipoServicio tipoServicio) {
        return jpaRepository.save(tipoServicio);
    }

    @Override
    public Optional<TipoServicio> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<TipoServicio> buscarActivosPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaIdAndActivoTrue(empresaId);
    }
}
