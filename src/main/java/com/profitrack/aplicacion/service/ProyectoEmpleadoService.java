package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoRequestDto;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoResponseDto;
import com.profitrack.dominio.model.*;
import com.profitrack.aplicacion.puerto.entrada.ProyectoEmpleadoUseCase;
import com.profitrack.dominio.puerto.salida.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoEmpleadoService implements ProyectoEmpleadoUseCase {

    private final ProyectoEmpleadoRepository peRepo;
    private final ProyectoRepository proyectoRepo;
    private final EmpleadoRepository empleadoRepo;
    private final HistorialCostoHoraRepository historialRepo;
    private final ProyectoCostoEmpleadoRepository costoRepo;

    @Override
    @Transactional
    public ProyectoEmpleadoResponseDto asignar(ProyectoEmpleadoRequestDto dto) {
        Proyecto proyecto = proyectoRepo.buscarPorId(dto.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        Empleado empleado = empleadoRepo.buscarPorId(dto.getEmpleadoId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        ProyectoEmpleado pe = peRepo.guardar(ProyectoEmpleado.builder()
                .proyecto(proyecto).empleado(empleado)
                .rolAsignado(dto.getRolAsignado())
                .fechaAsignacion(Instant.now())
                .activo(true).build());

        // aca congelamos la tarifa pa q si le suben el sueldo dspues no nos arruine el
        // margen de ese proyecto
        BigDecimal costoHora = historialRepo.buscarVigentePorEmpleado(empleado.getId())
                .map(HistorialCostoHoraEmpleado::getCostoHora)
                .orElse(BigDecimal.ZERO);

        costoRepo.guardar(ProyectoCostoEmpleado.builder()
                .proyecto(proyecto).empleado(empleado)
                .costoHora(costoHora).fechaInicio(LocalDate.now()).build());

        return toDto(pe, costoHora);
    }

    @Override
    public List<ProyectoEmpleadoResponseDto> listarPorProyecto(Long proyectoId) {
        return peRepo.buscarActivosPorProyecto(proyectoId).stream()
                .map(pe -> {
                    BigDecimal costo = costoRepo.buscarActivoPorProyectoYEmpleado(
                            pe.getProyecto().getId(), pe.getEmpleado().getId())
                            .map(ProyectoCostoEmpleado::getCostoHora)
                            .orElse(BigDecimal.ZERO);
                    return toDto(pe, costo);
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remover(Long id) {
        ProyectoEmpleado pe = peRepo.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        pe.setActivo(false);
        pe.setFechaRemocion(Instant.now());
        peRepo.guardar(pe);
    }

    private ProyectoEmpleadoResponseDto toDto(ProyectoEmpleado pe, BigDecimal costoHora) {
        return ProyectoEmpleadoResponseDto.builder()
                .id(pe.getId()).proyectoId(pe.getProyecto().getId())
                .proyectoNombre(pe.getProyecto().getNombre())
                .empleadoId(pe.getEmpleado().getId())
                .empleadoNombre(pe.getEmpleado().getNombres() + " " + pe.getEmpleado().getApellidos())
                .rolAsignado(pe.getRolAsignado())
                .fechaAsignacion(pe.getFechaAsignacion())
                .costoHoraCongelado(costoHora)
                .activo(pe.getActivo()).build();
    }
}
