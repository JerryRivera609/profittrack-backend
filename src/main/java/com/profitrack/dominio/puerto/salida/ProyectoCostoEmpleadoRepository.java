package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import java.util.Optional;
public interface ProyectoCostoEmpleadoRepository {
    ProyectoCostoEmpleado guardar(ProyectoCostoEmpleado pce);
    Optional<ProyectoCostoEmpleado> buscarActivoPorProyectoYEmpleado(Long proyectoId, Long empleadoId);
}
