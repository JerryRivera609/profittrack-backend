package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import java.util.List;
import java.util.Optional;

public interface ProyectoCostoEmpleadoRepository {
    ProyectoCostoEmpleado guardar(ProyectoCostoEmpleado pce);
    Optional<ProyectoCostoEmpleado> buscarActivoPorProyectoYEmpleado(Long proyectoId, Long empleadoId);
    List<ProyectoCostoEmpleado> listarPorProyecto(Long proyectoId);
}
