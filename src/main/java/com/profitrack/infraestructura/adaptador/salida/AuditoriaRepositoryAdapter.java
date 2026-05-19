package com.profitrack.infraestructura.adaptador.salida;
import com.profitrack.dominio.model.Auditoria;
import com.profitrack.dominio.puerto.salida.AuditoriaRepository;
import com.profitrack.infraestructura.repository.AuditoriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
@Component @RequiredArgsConstructor
public class AuditoriaRepositoryAdapter implements AuditoriaRepository {
    private final AuditoriaJpaRepository jpa;
    @Override public Auditoria guardar(Auditoria a) { return jpa.save(a); }
    @Override public List<Auditoria> buscarPorEntidad(String entidad, Long entidadId) { return jpa.findAllByEntidadAndEntidadId(entidad, entidadId); }
}
