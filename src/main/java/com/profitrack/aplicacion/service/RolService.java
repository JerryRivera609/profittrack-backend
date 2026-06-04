package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.rolDto.RolPatchDto;
import com.profitrack.aplicacion.dto.rolDto.RolRequestDto;
import com.profitrack.aplicacion.dto.rolDto.RolResponseDto;
import com.profitrack.aplicacion.puerto.entrada.RolUseCase;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.Rol;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.infraestructura.repository.RolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService implements RolUseCase {

    private final RolJpaRepository rolRepo;
    private final EmpresaRepository empresaRepo;

    @Override
    @Transactional
    public RolResponseDto crear(RolRequestDto dto) {
        Empresa empresa = empresaRepo.buscarPorId(dto.getEmpresaId())
                .filter(Empresa::getActivo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + dto.getEmpresaId()));

        validarNombreDisponible(dto.getNombre(), empresa.getId(), null);

        Rol rol = Rol.builder()
                .empresa(empresa)
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion())
                .build();
        rol.setActivo(true);

        return toDto(rolRepo.save(rol));
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDto obtenerPorId(Long id, Long empresaId) {
        return buscarRolEmpresa(id, empresaId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> listarActivos(Long empresaId) {
        return rolRepo.findAllByEmpresaIdAndActivoTrueOrderByNombreAsc(empresaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> listarInactivos(Long empresaId) {
        return rolRepo.findAllByEmpresaIdAndActivoFalseOrderByNombreAsc(empresaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RolResponseDto actualizar(Long id, RolPatchDto dto, Long empresaId) {
        Rol rol = buscarRolEmpresa(id, empresaId)
                .filter(Rol::getActivo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            validarNombreDisponible(dto.getNombre(), empresaId, id);
            rol.setNombre(dto.getNombre().trim());
        }
        if (dto.getDescripcion() != null) {
            rol.setDescripcion(dto.getDescripcion());
        }

        return toDto(rolRepo.save(rol));
    }

    @Override
    @Transactional
    public RolResponseDto reactivar(Long id, Long empresaId) {
        Rol rol = buscarRolEmpresa(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        validarNombreDisponible(rol.getNombre(), empresaId, rol.getId());
        rol.setActivo(true);
        return toDto(rolRepo.save(rol));
    }

    @Override
    @Transactional
    public void eliminar(Long id, Long empresaId) {
        Rol rol = buscarRolEmpresa(id, empresaId)
                .filter(Rol::getActivo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        rol.setActivo(false);
        rolRepo.save(rol);
    }

    private void validarNombreDisponible(String nombre, Long empresaId, Long rolActualId) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }

        rolRepo.findByEmpresaIdAndNombreIgnoreCase(empresaId, nombre.trim())
                .filter(rol -> rolActualId == null || !rol.getId().equals(rolActualId))
                .ifPresent(rol -> {
                    throw new RuntimeException("Ya existe un rol con el nombre: " + nombre.trim());
                });
    }

    private java.util.Optional<Rol> buscarRolEmpresa(Long id, Long empresaId) {
        return rolRepo.findById(id)
                .filter(rol -> rol.getEmpresa() != null && rol.getEmpresa().getId().equals(empresaId));
    }

    private RolResponseDto toDto(Rol rol) {
        return RolResponseDto.builder()
                .id(rol.getId())
                .empresaId(rol.getEmpresa() != null ? rol.getEmpresa().getId() : null)
                .nombreEmpresa(rol.getEmpresa() != null ? rol.getEmpresa().getNombre() : null)
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .activo(rol.getActivo())
                .build();
    }
}
