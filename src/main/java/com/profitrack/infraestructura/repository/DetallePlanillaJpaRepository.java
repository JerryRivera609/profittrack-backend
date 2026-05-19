package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.DetallePlanilla;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DetallePlanillaJpaRepository extends JpaRepository<DetallePlanilla, Long> {
    List<DetallePlanilla> findAllByPlanillaId(Long planillaId);
}
