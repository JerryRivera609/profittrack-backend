package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import com.profitrack.dominio.puerto.salida.ProyectoCostoEmpleadoRepository;
import com.profitrack.infraestructura.repository.ProyectoCostoEmpleadoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProyectoCostoEmpleadoRepositoryAdapter implements ProyectoCostoEmpleadoRepository {
    private final ProyectoCostoEmpleadoJpaRepository jpa;

    @Override
    public ProyectoCostoEmpleado guardar(ProyectoCostoEmpleado pce) {
        return jpa.save(pce);
    }

    @Override
    public Optional<ProyectoCostoEmpleado> buscarActivoPorProyectoYEmpleado(Long proyectoId, Long empleadoId) {
        return jpa.findByProyectoIdAndEmpleadoIdAndFechaFinIsNull(proyectoId, empleadoId);
    }
}
