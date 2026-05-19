package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteJpaRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByEmpresaIdAndActivoTrue(Long empresaId);
}
