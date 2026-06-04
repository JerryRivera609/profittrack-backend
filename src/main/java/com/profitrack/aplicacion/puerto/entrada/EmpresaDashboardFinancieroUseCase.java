package com.profitrack.aplicacion.puerto.entrada;

import com.profitrack.aplicacion.dto.empresaDashboardDto.EmpresaDashboardFinancieroResponseDto;

public interface EmpresaDashboardFinancieroUseCase {
    EmpresaDashboardFinancieroResponseDto obtener(Long empresaId, Long empleadoId, String rolGlobal);
}
