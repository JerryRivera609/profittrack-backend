package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.MetricaProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MetricaProyectoJpaRepository extends JpaRepository<MetricaProyecto, Long> {
    List<MetricaProyecto> findAllByProyectoId(Long proyectoId);
}
