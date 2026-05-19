package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.TipoTarea;
import java.util.List;
import java.util.Optional;
public interface TipoTareaRepository {
    TipoTarea guardar(TipoTarea t);
    Optional<TipoTarea> buscarPorId(Long id);
    List<TipoTarea> buscarPorEmpresa(Long empresaId);
}
