package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolJpaRepository extends JpaRepository<Rol, Long> {
}
