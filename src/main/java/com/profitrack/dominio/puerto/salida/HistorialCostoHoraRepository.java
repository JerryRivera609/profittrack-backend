package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.HistorialCostoHoraEmpleado;
import java.util.Optional;
public interface HistorialCostoHoraRepository {
    HistorialCostoHoraEmpleado guardar(HistorialCostoHoraEmpleado h);
    Optional<HistorialCostoHoraEmpleado> buscarVigentePorEmpleado(Long empleadoId);
}
