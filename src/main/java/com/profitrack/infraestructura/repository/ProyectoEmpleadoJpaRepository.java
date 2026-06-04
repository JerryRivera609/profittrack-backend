package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.ProyectoEmpleado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProyectoEmpleadoJpaRepository extends JpaRepository<ProyectoEmpleado, Long> {
    @Override
    @EntityGraph(attributePaths = {"proyecto", "proyecto.empresa", "proyecto.liderEmpleado", "empleado"})
    Optional<ProyectoEmpleado> findById(Long id);

    @EntityGraph(attributePaths = {"proyecto", "proyecto.empresa", "proyecto.liderEmpleado", "empleado"})
    List<ProyectoEmpleado> findAllByProyectoIdAndActivoTrue(Long proyectoId);

    @EntityGraph(attributePaths = {"proyecto", "proyecto.empresa", "proyecto.liderEmpleado", "empleado"})
    List<ProyectoEmpleado> findAllByEmpleadoIdAndActivoTrue(Long empleadoId);

    @EntityGraph(attributePaths = {"proyecto", "proyecto.empresa", "proyecto.liderEmpleado", "empleado"})
    Optional<ProyectoEmpleado> findByProyectoIdAndEmpleadoIdAndActivoTrue(Long proyectoId, Long empleadoId);
}
