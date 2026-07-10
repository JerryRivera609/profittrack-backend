package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.aplicacion.dto.empresaDto.EmpresaRequestDto;
import com.profitrack.aplicacion.dto.empresaDto.EmpresaPatchDto;
import com.profitrack.aplicacion.dto.empresaDto.EmpresaResponseDto;
import com.profitrack.aplicacion.dto.empleadoDto.EmpleadoRequestDto;
import com.profitrack.aplicacion.dto.empleadoDto.EmpleadoResponseDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.dto.rolDto.RolResponseDto;
import com.profitrack.aplicacion.puerto.entrada.AdministradorSaasUseCase;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorSaasUseCase adminUseCase;
    private final SecurityContextUtils securityContext;

    @GetMapping("/empresas")
    public ResponseEntity<List<EmpresaResponseDto>> listarEmpresas() {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.listarTodasLasEmpresas());
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<EmpresaResponseDto> obtenerEmpresa(@PathVariable Long id) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.obtenerEmpresaPorId(id));
    }

    @PostMapping("/empresas")
    public ResponseEntity<EmpresaResponseDto> crearEmpresa(@Valid @RequestBody EmpresaRequestDto dto) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUseCase.crearEmpresa(dto));
    }

    @PatchMapping("/empresas/{id}")
    public ResponseEntity<EmpresaResponseDto> actualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaPatchDto dto) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.actualizarEmpresa(id, dto));
    }

    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        securityContext.validarSaasAdmin();
        adminUseCase.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/empresas/{id}/estado")
    public ResponseEntity<EmpresaResponseDto> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.cambiarEstadoEmpresa(id, activo));
    }

    @GetMapping("/empresas/{empresaId}/empleados")
    public ResponseEntity<List<EmpleadoResponseDto>> listarEmpleados(@PathVariable Long empresaId) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.listarEmpleadosPorEmpresa(empresaId));
    }

    @PostMapping("/empresas/{empresaId}/empleados")
    public ResponseEntity<EmpleadoResponseDto> crearEmpleado(
            @PathVariable Long empresaId,
            @Valid @RequestBody EmpleadoRequestDto dto) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUseCase.crearEmpleadoParaEmpresa(empresaId, dto));
    }

    @GetMapping("/empresas/{empresaId}/proyectos")
    public ResponseEntity<List<ProyectoResponseDto>> listarProyectos(@PathVariable Long empresaId) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.listarProyectosPorEmpresa(empresaId));
    }

    @GetMapping("/empresas/{empresaId}/roles")
    public ResponseEntity<List<RolResponseDto>> listarRoles(@PathVariable Long empresaId) {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.listarRolesPorEmpresa(empresaId));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        securityContext.validarSaasAdmin();
        return ResponseEntity.ok(adminUseCase.obtenerEstadisticasGlobales());
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> obtenerPerfil() {
        securityContext.validarSaasAdmin();
        Long adminId = securityContext.getUserId();
        return ResponseEntity.ok(adminUseCase.obtenerPerfilAdmin(adminId));
    }
}
