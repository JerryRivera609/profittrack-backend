package com.profitrack.dominio.service;

import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaPatchDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaRequestDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaResponseDto;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.TipoTarea;
import com.profitrack.dominio.puerto.entrada.TipoTareaUseCase;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.dominio.puerto.salida.TipoTareaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoTareaService implements TipoTareaUseCase {

    private final TipoTareaRepository tipoTareaRepo;
    private final EmpresaRepository empresaRepo;

    @Override
    @Transactional
    public TipoTareaResponseDto crear(TipoTareaRequestDto dto) {
        Empresa empresa = empresaRepo.buscarPorId(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        TipoTarea ts = TipoTarea.builder()
                .empresa(empresa)
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
        ts.setActivo(true);

        return toDto(tipoTareaRepo.guardar(ts));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTareaResponseDto obtenerPorId(Long id) {
        TipoTarea ts = tipoTareaRepo.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("TipoTarea no encontrado"));
        return toDto(ts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoTareaResponseDto> listarActivosPorEmpresa(Long empresaId) {
        return tipoTareaRepo.buscarActivosPorEmpresa(empresaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TipoTareaResponseDto actualizar(Long id, TipoTareaPatchDto dto) {
        TipoTarea ts = tipoTareaRepo.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("TipoTarea no encontrado"));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            ts.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            ts.setDescripcion(dto.getDescripcion());
        }

        return toDto(tipoTareaRepo.guardar(ts));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        TipoTarea ts = tipoTareaRepo.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("TipoTarea no encontrado"));
        ts.setActivo(false);
        tipoTareaRepo.guardar(ts);
    }

    private TipoTareaResponseDto toDto(TipoTarea ts) {
        return TipoTareaResponseDto.builder()
                .id(ts.getId())
                .empresaId(ts.getEmpresa() != null ? ts.getEmpresa().getId() : null)
                .nombreEmpresa(ts.getEmpresa() != null ? ts.getEmpresa().getNombre() : null)
                .nombre(ts.getNombre())
                .descripcion(ts.getDescripcion())
                .activo(ts.getActivo())
                .build();
    }
}
