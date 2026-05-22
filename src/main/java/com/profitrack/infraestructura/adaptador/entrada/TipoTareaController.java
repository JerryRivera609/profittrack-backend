package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaPatchDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaRequestDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaResponseDto;
import com.profitrack.dominio.puerto.entrada.TipoTareaUseCase;
import com.profitrack.infraestructura.seguridad.RolConstantes;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-tarea")
@RequiredArgsConstructor
public class TipoTareaController {

    private final TipoTareaUseCase tipoTareaUseCase;
    private final SecurityContextUtils securityUtils;

    @PostMapping
    public ResponseEntity<TipoTareaResponseDto> crear(@Valid @RequestBody TipoTareaRequestDto dto) {
        dto.setEmpresaId(securityUtils.getEmpresaId());
        securityUtils.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER,
                RolConstantes.ADMINISTRADOR);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoTareaUseCase.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<TipoTareaResponseDto>> listarPorEmpresa() {
        Long empresaId = securityUtils.getEmpresaId();
        return ResponseEntity.ok(tipoTareaUseCase.listarActivosPorEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTareaResponseDto> obtenerPorId(@PathVariable Long id) {
        TipoTareaResponseDto res = tipoTareaUseCase.obtenerPorId(id);
        validarEmpresa(res.getEmpresaId());
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoTareaResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoTareaPatchDto dto) {
        TipoTareaResponseDto res = tipoTareaUseCase.obtenerPorId(id);
        validarEmpresa(res.getEmpresaId());
        securityUtils.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER,
                RolConstantes.ADMINISTRADOR);
        return ResponseEntity.ok(tipoTareaUseCase.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        TipoTareaResponseDto res = tipoTareaUseCase.obtenerPorId(id);
        validarEmpresa(res.getEmpresaId());
        securityUtils.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER,
                RolConstantes.ADMINISTRADOR);
        tipoTareaUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarEmpresa(Long empresaId) {
        if (!securityUtils.getEmpresaId().equals(empresaId)) {
            throw new RuntimeException("Acceso denegado: el recurso no pertenece a su empresa");
        }
    }
}
