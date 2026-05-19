package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.Planilla;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PlanillaJpaRepository extends JpaRepository<Planilla, Long> {
    List<Planilla> findAllByEmpresaId(Long empresaId);
}
