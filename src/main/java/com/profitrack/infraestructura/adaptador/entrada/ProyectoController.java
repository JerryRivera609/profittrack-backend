package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.proyectoDto.ProyectoPatchDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoRequestDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.dto.ownerDashboardDto.OwnerDashboardResponseDto;
import com.profitrack.aplicacion.puerto.entrada.OwnerDashboardUseCase;
import com.profitrack.aplicacion.puerto.entrada.ProyectoUseCase;
import com.profitrack.infraestructura.seguridad.RolConstantes;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoUseCase proyectoUseCase;
    private final OwnerDashboardUseCase ownerDashboardUseCase;
    private final SecurityContextUtils securityContext;

    @PostMapping
    public ResponseEntity<ProyectoResponseDto> crear(@Valid @RequestBody ProyectoRequestDto dto) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        dto.setEmpresaId(securityContext.getEmpresaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoUseCase.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDto> obtenerPorId(@PathVariable Long id) {
        securityContext.validarAccesoProyecto(id);
        return ResponseEntity.ok(proyectoUseCase.obtenerPorIdParaUsuario(
                id,
                empleadoIdParaContexto(),
                rolGlobalParaContexto()));
    }

    @GetMapping("/{id}/dashboard-owner")
    public ResponseEntity<OwnerDashboardResponseDto> dashboardOwner(@PathVariable Long id) {
        securityContext.validarRolOProyectoLider(id, RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        return ResponseEntity.ok(ownerDashboardUseCase.obtenerPorProyecto(
                id,
                securityContext.getEmpresaId(),
                empleadoIdParaContexto(),
                rolGlobalParaContexto()));
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponseDto>> listarPorEmpresa() {
        Long empresaId = securityContext.getEmpresaId();
        String rol = securityContext.getRolNombre();
        String tipo = securityContext.getTipo();

        boolean esAdmin = "duenio".equalsIgnoreCase(tipo) ||
                RolConstantes.OWNER.equalsIgnoreCase(rol) ||
                RolConstantes.GERENTE.equalsIgnoreCase(rol) ||
                RolConstantes.PM.equalsIgnoreCase(rol) ||
                RolConstantes.ADMINISTRADOR.equalsIgnoreCase(rol);

        if (esAdmin) {
            return ResponseEntity.ok(proyectoUseCase.listarActivosPorEmpresaParaUsuario(
                    empresaId,
                    empleadoIdParaContexto(),
                    rolGlobalParaContexto()));
        } else {
            Long empleadoId = securityContext.getUserId();
            return ResponseEntity.ok(proyectoUseCase.listarProyectosAsignados(empleadoId, empresaId));
        }
    }

    @GetMapping("/mis-proyectos")
    public ResponseEntity<List<ProyectoResponseDto>> listarMisProyectos() {
        Long empresaId = securityContext.getEmpresaId();
        Long empleadoId = securityContext.getUserId();
        return ResponseEntity.ok(proyectoUseCase.listarProyectosAsignados(empleadoId, empresaId));
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<ProyectoResponseDto>> listarInactivosPorEmpresa() {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        Long empresaId = securityContext.getEmpresaId();
        return ResponseEntity.ok(proyectoUseCase.listarInactivosPorEmpresaParaUsuario(
                empresaId,
                empleadoIdParaContexto(),
                rolGlobalParaContexto()));
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<ProyectoResponseDto> reactivar(@PathVariable Long id) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        return ResponseEntity.ok(proyectoUseCase.reactivar(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProyectoResponseDto> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProyectoPatchDto dto) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        return ResponseEntity.ok(proyectoUseCase.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        securityContext.validarRol(RolConstantes.PM, RolConstantes.GERENTE, RolConstantes.OWNER);
        proyectoUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Long empleadoIdParaContexto() {
        return "empleado".equalsIgnoreCase(securityContext.getTipo()) ? securityContext.getUserId() : null;
    }

    private String rolGlobalParaContexto() {
        return esAdminProyecto() ? securityContext.getRolNombre() : null;
    }

    private boolean esAdminProyecto() {
        return "duenio".equalsIgnoreCase(securityContext.getTipo()) ||
                securityContext.tieneRol(
                        RolConstantes.OWNER,
                        RolConstantes.GERENTE,
                        RolConstantes.PM,
                        RolConstantes.ADMINISTRADOR);
    }
}
