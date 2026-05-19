package com.profitrack.infraestructura.adaptador.salida;
import com.profitrack.dominio.model.TipoTarea;
import com.profitrack.dominio.puerto.salida.TipoTareaRepository;
import com.profitrack.infraestructura.repository.TipoTareaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
@Component @RequiredArgsConstructor
public class TipoTareaRepositoryAdapter implements TipoTareaRepository {
    private final TipoTareaJpaRepository jpa;
    @Override public TipoTarea guardar(TipoTarea t) { return jpa.save(t); }
    @Override public Optional<TipoTarea> buscarPorId(Long id) { return jpa.findById(id); }
    @Override public List<TipoTarea> buscarPorEmpresa(Long empresaId) { return jpa.findAllByEmpresaId(empresaId); }
}
