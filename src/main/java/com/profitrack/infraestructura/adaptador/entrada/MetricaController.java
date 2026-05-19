package com.profitrack.infraestructura.adaptador.entrada;

import com.profitrack.dominio.model.MetricaProyecto;
import com.profitrack.dominio.puerto.salida.MetricaProyectoRepository;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api/metricas") @RequiredArgsConstructor
public class MetricaController {
    private final MetricaProyectoRepository metricaRepo;
    private final SecurityContextUtils ctx;

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<MetricaDto>> porProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(metricaRepo.buscarPorProyecto(proyectoId).stream()
                .map(this::toDto).collect(Collectors.toList()));
    }

    @Data @Builder
    public static class MetricaDto {
        private Long id; private Long proyectoId; private LocalDate fechaSnapshot;
        private BigDecimal costoPlanificado; private BigDecimal costoReal;
        private BigDecimal ingresoPlanificado; private BigDecimal ingresoReal;
        private BigDecimal margenPlanificado; private BigDecimal margenReal;
        private BigDecimal horasPlanificadas; private BigDecimal horasReales;
    }

    private MetricaDto toDto(MetricaProyecto m) {
        return MetricaDto.builder().id(m.getId()).proyectoId(m.getProyecto().getId())
                .fechaSnapshot(m.getFechaSnapshot())
                .costoPlanificado(m.getCostoPlanificado()).costoReal(m.getCostoReal())
                .ingresoPlanificado(m.getIngresoPlanificado()).ingresoReal(m.getIngresoReal())
                .margenPlanificado(m.getMargenPlanificado()).margenReal(m.getMargenReal())
                .horasPlanificadas(m.getHorasPlanificadas()).horasReales(m.getHorasReales()).build();
    }
}
