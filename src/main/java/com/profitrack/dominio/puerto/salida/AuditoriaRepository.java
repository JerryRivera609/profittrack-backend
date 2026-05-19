package com.profitrack.dominio.puerto.salida;
import com.profitrack.dominio.model.Auditoria;
import java.util.List;
public interface AuditoriaRepository {
    Auditoria guardar(Auditoria a);
    List<Auditoria> buscarPorEntidad(String entidad, Long entidadId);
}
