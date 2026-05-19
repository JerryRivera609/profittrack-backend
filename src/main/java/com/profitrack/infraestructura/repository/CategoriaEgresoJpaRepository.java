package com.profitrack.infraestructura.repository;
import com.profitrack.dominio.model.CategoriaEgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CategoriaEgresoJpaRepository extends JpaRepository<CategoriaEgreso, Long> {
    List<CategoriaEgreso> findAllByEmpresaId(Long empresaId);
}
