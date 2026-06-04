package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.Proyecto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProyectoJpaRepository extends JpaRepository<Proyecto, Long> {
    @Override
    @EntityGraph(attributePaths = {"empresa", "cliente", "tipoServicio", "liderEmpleado"})
    Optional<Proyecto> findById(Long id);

    @EntityGraph(attributePaths = {"empresa", "cliente", "tipoServicio", "liderEmpleado"})
    List<Proyecto> findAllByEmpresaIdAndActivoTrue(Long empresaId);

    @EntityGraph(attributePaths = {"empresa", "cliente", "tipoServicio", "liderEmpleado"})
    List<Proyecto> findAllByEmpresaIdAndActivoFalse(Long empresaId);
}
