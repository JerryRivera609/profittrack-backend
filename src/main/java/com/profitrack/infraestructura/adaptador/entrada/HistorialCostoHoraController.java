package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.historialCostoHoraDto.HistorialCostoHoraRequestDto;
import com.profitrack.aplicacion.dto.historialCostoHoraDto.HistorialCostoHoraResponseDto;
import com.profitrack.dominio.puerto.entrada.HistorialCostoHoraUseCase;
import com.profitrack.infraestructura.seguridad.RolConstantes;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-costo-hora")
@RequiredArgsConstructor
public class HistorialCostoHoraController {

    private final HistorialCostoHoraUseCase useCase;
    private final SecurityContextUtils securityContext;

    @PostMapping
    public ResponseEntity<HistorialCostoHoraResponseDto> registrarCosto(
            @Valid @RequestBody HistorialCostoHoraRequestDto dto) {
        securityContext.validarRol(RolConstantes.ADMINISTRADOR, RolConstantes.OWNER, RolConstantes.GERENTE);
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.registrarCosto(dto));
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<HistorialCostoHoraResponseDto>> listarPorEmpleado(@PathVariable Long empleadoId) {
        securityContext.validarRol(RolConstantes.ADMINISTRADOR, RolConstantes.OWNER, RolConstantes.GERENTE);
        return ResponseEntity.ok(useCase.listarPorEmpleado(empleadoId));
    }
}
