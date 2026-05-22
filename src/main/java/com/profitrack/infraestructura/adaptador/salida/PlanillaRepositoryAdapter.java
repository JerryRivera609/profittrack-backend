package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.DetallePlanilla;
import com.profitrack.dominio.model.Planilla;
import com.profitrack.dominio.puerto.salida.PlanillaRepository;
import com.profitrack.infraestructura.repository.DetallePlanillaJpaRepository;
import com.profitrack.infraestructura.repository.PlanillaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlanillaRepositoryAdapter implements PlanillaRepository {
    private final PlanillaJpaRepository planillaJpa;
    private final DetallePlanillaJpaRepository detalleJpa;

    @Override
    public Planilla guardar(Planilla p) {
        return planillaJpa.save(p);
    }

    @Override
    public Optional<Planilla> buscarPorId(Long id) {
        return planillaJpa.findById(id);
    }

    @Override
    public List<Planilla> buscarPorEmpresa(Long empresaId) {
        return planillaJpa.findAllByEmpresaId(empresaId);
    }

    @Override
    public DetallePlanilla guardarDetalle(DetallePlanilla d) {
        return detalleJpa.save(d);
    }

    @Override
    public List<DetallePlanilla> buscarDetallesPorPlanilla(Long planillaId) {
        return detalleJpa.findAllByPlanillaId(planillaId);
    }
}
