package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.MetricaProyecto;
import java.util.List;
public interface MetricaProyectoRepository {
    MetricaProyecto guardar(MetricaProyecto m);
    List<MetricaProyecto> buscarPorProyecto(Long proyectoId);
}
