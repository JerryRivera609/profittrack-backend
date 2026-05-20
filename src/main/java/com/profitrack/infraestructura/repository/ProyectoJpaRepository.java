package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoJpaRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findAllByEmpresaIdAndActivoTrue(Long empresaId);
    List<Proyecto> findAllByEmpresaIdAndActivoFalse(Long empresaId);
}
