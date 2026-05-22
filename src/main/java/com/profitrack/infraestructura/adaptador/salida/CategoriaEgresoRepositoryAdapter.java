package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.CategoriaEgreso;
import com.profitrack.dominio.puerto.salida.CategoriaEgresoRepository;
import com.profitrack.infraestructura.repository.CategoriaEgresoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaEgresoRepositoryAdapter implements CategoriaEgresoRepository {
    private final CategoriaEgresoJpaRepository jpa;

    @Override
    public CategoriaEgreso guardar(CategoriaEgreso c) {
        return jpa.save(c);
    }

    @Override
    public Optional<CategoriaEgreso> buscarPorId(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<CategoriaEgreso> buscarPorEmpresa(Long empresaId) {
        return jpa.findAllByEmpresaId(empresaId);
    }
}
