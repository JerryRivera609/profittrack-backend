package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoRequestDto;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoResponseDto;
import com.profitrack.dominio.puerto.entrada.ProyectoEmpleadoUseCase;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proyecto-empleados")
@RequiredArgsConstructor
public class ProyectoEmpleadoController {
    private final ProyectoEmpleadoUseCase useCase;
    private final SecurityContextUtils securityContext;

    @PostMapping
    public ResponseEntity<ProyectoEmpleadoResponseDto> asignar(@Valid @RequestBody ProyectoEmpleadoRequestDto dto) {
        securityContext.validarRol("PM", "Gerente", "Owner");
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.asignar(dto));
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ProyectoEmpleadoResponseDto>> listar(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(useCase.listarPorProyecto(proyectoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        securityContext.validarRol("PM", "Gerente", "Owner");
        useCase.remover(id);
        return ResponseEntity.noContent().build();
    }
}
