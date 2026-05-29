package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolJpaRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByEmpresaIdAndNombreIgnoreCase(Long empresaId, String nombre);

    List<Rol> findAllByEmpresaIdAndActivoTrueOrderByNombreAsc(Long empresaId);

    List<Rol> findAllByEmpresaIdAndActivoFalseOrderByNombreAsc(Long empresaId);
}
