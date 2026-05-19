package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.Planilla;
import com.profitrack.dominio.model.DetallePlanilla;
import java.util.List;
import java.util.Optional;
public interface PlanillaRepository {
    Planilla guardar(Planilla p);
    Optional<Planilla> buscarPorId(Long id);
    List<Planilla> buscarPorEmpresa(Long empresaId);
    DetallePlanilla guardarDetalle(DetallePlanilla d);
    List<DetallePlanilla> buscarDetallesPorPlanilla(Long planillaId);
}
