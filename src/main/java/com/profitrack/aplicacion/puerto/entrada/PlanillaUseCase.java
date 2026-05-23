package com.profitrack.aplicacion.puerto.entrada;
import com.profitrack.aplicacion.dto.planillaDto.PlanillaRequestDto;
import com.profitrack.aplicacion.dto.planillaDto.PlanillaResponseDto;
import java.util.List;
public interface PlanillaUseCase {
    PlanillaResponseDto crear(PlanillaRequestDto dto);
    PlanillaResponseDto obtenerPorId(Long id);
    List<PlanillaResponseDto> listarPorEmpresa(Long empresaId);
}
