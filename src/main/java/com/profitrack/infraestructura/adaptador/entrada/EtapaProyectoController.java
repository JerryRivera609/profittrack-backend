package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoPatchDto;
import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoRequestDto;
import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoResponseDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.puerto.entrada.EtapaProyectoUseCase;
import com.profitrack.aplicacion.puerto.entrada.ProyectoUseCase;
import com.profitrack.infraestructura.seguridad.RolConstantes;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EtapaProyectoController {

    private final EtapaProyectoUseCase etapaUseCase;
    private final ProyectoUseCase proyectoUseCase;
    private final SecurityContextUtils securityContext;

    @PostMapping("/proyectos/{proyectoId}/etapas")
    public ResponseEntity<EtapaProyectoResponseDto> crear(
            @PathVariable Long proyectoId,
            @Valid @RequestBody EtapaProyectoRequestDto dto) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        validarProyectoEmpresa(proyectoId);
        dto.setProyectoId(proyectoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(etapaUseCase.crear(dto));
    }

    @GetMapping("/proyectos/{proyectoId}/etapas")
    public ResponseEntity<List<EtapaProyectoResponseDto>> listarPorProyecto(@PathVariable Long proyectoId) {
        validarProyectoEmpresa(proyectoId);
        return ResponseEntity.ok(etapaUseCase.listarPorProyecto(proyectoId));
    }

    @GetMapping("/proyectos/{proyectoId}/etapas/inactivas")
    public ResponseEntity<List<EtapaProyectoResponseDto>> listarInactivasPorProyecto(@PathVariable Long proyectoId) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        validarProyectoEmpresa(proyectoId);
        return ResponseEntity.ok(etapaUseCase.listarInactivasPorProyecto(proyectoId));
    }

    @GetMapping("/etapas-proyecto/{id}")
    public ResponseEntity<EtapaProyectoResponseDto> obtenerPorId(@PathVariable Long id) {
        EtapaProyectoResponseDto res = etapaUseCase.obtenerPorId(id);
        validarEmpresa(res.getEmpresaId());
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/etapas-proyecto/{id}")
    public ResponseEntity<EtapaProyectoResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EtapaProyectoPatchDto dto) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        EtapaProyectoResponseDto actual = etapaUseCase.obtenerPorId(id);
        validarEmpresa(actual.getEmpresaId());
        return ResponseEntity.ok(etapaUseCase.actualizar(id, dto));
    }

    @PatchMapping("/etapas-proyecto/{id}/reactivar")
    public ResponseEntity<EtapaProyectoResponseDto> reactivar(@PathVariable Long id) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        EtapaProyectoResponseDto actual = etapaUseCase.obtenerPorId(id);
        validarEmpresa(actual.getEmpresaId());
        return ResponseEntity.ok(etapaUseCase.reactivar(id));
    }

    @DeleteMapping("/etapas-proyecto/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        EtapaProyectoResponseDto actual = etapaUseCase.obtenerPorId(id);
        validarEmpresa(actual.getEmpresaId());
        etapaUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarProyectoEmpresa(Long proyectoId) {
        ProyectoResponseDto proyecto = proyectoUseCase.obtenerPorId(proyectoId);
        validarEmpresa(proyecto.getEmpresaId());
    }

    private void validarEmpresa(Long empresaId) {
        if (!securityContext.getEmpresaId().equals(empresaId)) {
            throw new RuntimeException("Acceso denegado: el recurso no pertenece a su empresa");
        }
    }
}
