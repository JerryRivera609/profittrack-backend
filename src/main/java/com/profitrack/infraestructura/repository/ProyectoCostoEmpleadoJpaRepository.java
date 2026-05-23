package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProyectoCostoEmpleadoJpaRepository extends JpaRepository<ProyectoCostoEmpleado, Long> {
    Optional<ProyectoCostoEmpleado> findByProyectoIdAndEmpleadoIdAndFechaFinIsNull(Long proyectoId, Long empleadoId);
}
