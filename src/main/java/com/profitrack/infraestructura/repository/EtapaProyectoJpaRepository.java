package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.EtapaProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtapaProyectoJpaRepository extends JpaRepository<EtapaProyecto, Long> {
    List<EtapaProyecto> findAllByProyectoIdAndActivoTrueOrderByOrdenAsc(Long proyectoId);
    List<EtapaProyecto> findAllByProyectoIdAndActivoFalseOrderByOrdenAsc(Long proyectoId);
}
