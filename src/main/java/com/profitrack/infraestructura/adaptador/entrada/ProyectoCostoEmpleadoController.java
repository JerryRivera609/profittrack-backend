package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.proyectoCostoEmpleadoDto.ProyectoCostoEmpleadoResponseDto;
import com.profitrack.aplicacion.puerto.entrada.ProyectoCostoEmpleadoUseCase;
import com.profitrack.infraestructura.seguridad.RolConstantes;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-costo-empleado")
@RequiredArgsConstructor
public class ProyectoCostoEmpleadoController {

    private final ProyectoCostoEmpleadoUseCase useCase;
    private final SecurityContextUtils ctx;

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ProyectoCostoEmpleadoResponseDto>> listarPorProyecto(@PathVariable Long proyectoId) {
        ctx.validarRolOProyectoLider(proyectoId, RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        return ResponseEntity.ok(useCase.listarPorProyecto(proyectoId));
    }
}
