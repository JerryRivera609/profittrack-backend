package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.TipoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TipoTareaJpaRepository extends JpaRepository<TipoTarea, Long> {
    List<TipoTarea> findAllByEmpresaId(Long empresaId);
}
