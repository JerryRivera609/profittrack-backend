package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.proyectoCostoEmpleadoDto.ProyectoCostoEmpleadoResponseDto;
import com.profitrack.aplicacion.puerto.entrada.ProyectoCostoEmpleadoUseCase;
import com.profitrack.dominio.model.ProyectoCostoEmpleado;
import com.profitrack.dominio.puerto.salida.ProyectoCostoEmpleadoRepository;
import com.profitrack.dominio.puerto.salida.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoCostoEmpleadoService implements ProyectoCostoEmpleadoUseCase {

    private final ProyectoCostoEmpleadoRepository costoRepo;
    private final ProyectoRepository proyectoRepo;

    @Override
    public List<ProyectoCostoEmpleadoResponseDto> listarPorProyecto(Long proyectoId) {
        proyectoRepo.buscarPorId(proyectoId)
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + proyectoId));

        return costoRepo.listarPorProyecto(proyectoId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProyectoCostoEmpleadoResponseDto toDto(ProyectoCostoEmpleado costo) {
        return ProyectoCostoEmpleadoResponseDto.builder()
                .id(costo.getId())
                .proyectoId(costo.getProyecto().getId())
                .proyectoNombre(costo.getProyecto().getNombre())
                .empleadoId(costo.getEmpleado().getId())
                .empleadoNombre(costo.getEmpleado().getNombres() + " " + costo.getEmpleado().getApellidos())
                .costoHora(costo.getCostoHora())
                .fechaInicio(costo.getFechaInicio())
                .fechaFin(costo.getFechaFin())
                .vigente(costo.getFechaFin() == null)
                .activo(costo.getActivo())
                .build();
    }
}
