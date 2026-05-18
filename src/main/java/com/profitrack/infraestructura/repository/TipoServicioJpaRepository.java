package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoServicioJpaRepository extends JpaRepository<TipoServicio, Long> {
    List<TipoServicio> findAllByEmpresaIdAndActivoTrue(Long empresaId);
}
