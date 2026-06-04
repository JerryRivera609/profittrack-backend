package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProyectoCostoEmpleadoJpaRepository extends JpaRepository<ProyectoCostoEmpleado, Long> {
    @EntityGraph(attributePaths = {"proyecto", "empleado"})
    Optional<ProyectoCostoEmpleado> findByProyectoIdAndEmpleadoIdAndFechaFinIsNull(Long proyectoId, Long empleadoId);

    @EntityGraph(attributePaths = {"proyecto", "empleado"})
    List<ProyectoCostoEmpleado> findAllByProyectoIdOrderByFechaInicioDesc(Long proyectoId);
}
