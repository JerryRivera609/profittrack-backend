package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditoriaJpaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findAllByEntidadAndEntidadId(String entidad, Long entidadId);
}
