package com.profitrack.aplicacion.puerto.entrada;

import com.profitrack.aplicacion.dto.ownerDashboardDto.OwnerDashboardResponseDto;

public interface OwnerDashboardUseCase {
    OwnerDashboardResponseDto obtenerPorProyecto(Long proyectoId, Long empresaId, Long empleadoId, String rolGlobal);
}
