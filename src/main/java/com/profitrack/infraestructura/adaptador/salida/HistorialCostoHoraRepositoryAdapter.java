package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.HistorialCostoHoraEmpleado;
import com.profitrack.dominio.puerto.salida.HistorialCostoHoraRepository;
import com.profitrack.infraestructura.repository.HistorialCostoHoraJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HistorialCostoHoraRepositoryAdapter implements HistorialCostoHoraRepository {
    private final HistorialCostoHoraJpaRepository jpa;

    @Override
    public HistorialCostoHoraEmpleado guardar(HistorialCostoHoraEmpleado h) {
        return jpa.save(h);
    }

    @Override
    public Optional<HistorialCostoHoraEmpleado> buscarVigentePorEmpleado(Long empleadoId) {
        return jpa.findFirstByEmpleadoIdAndFechaFinIsNullOrderByFechaInicioDesc(empleadoId);
    }

    @Override
    public List<HistorialCostoHoraEmpleado> listarPorEmpleado(Long empleadoId) {
        return jpa.findAllByEmpleadoIdOrderByFechaInicioDesc(empleadoId);
    }
}
