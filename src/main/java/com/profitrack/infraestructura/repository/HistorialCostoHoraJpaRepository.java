package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.HistorialCostoHoraEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface HistorialCostoHoraJpaRepository extends JpaRepository<HistorialCostoHoraEmpleado, Long> {
    Optional<HistorialCostoHoraEmpleado> findFirstByEmpleadoIdAndFechaFinIsNullOrderByFechaInicioDesc(Long empleadoId);
    List<HistorialCostoHoraEmpleado> findAllByEmpleadoIdOrderByFechaInicioDesc(Long empleadoId);
}
