package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.CategoriaEgreso;
import java.util.List;
import java.util.Optional;
public interface CategoriaEgresoRepository {
    CategoriaEgreso guardar(CategoriaEgreso c);
    Optional<CategoriaEgreso> buscarPorId(Long id);
    List<CategoriaEgreso> buscarPorEmpresa(Long empresaId);
}
