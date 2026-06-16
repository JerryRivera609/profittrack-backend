package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.empresaDashboardDto.EmpresaDashboardFinancieroResponseDto;
import com.profitrack.aplicacion.dto.empresaDto.EmpresaResponseDto;
import com.profitrack.aplicacion.dto.ingresoDto.IngresoResponseDto;
import com.profitrack.aplicacion.dto.metricaDto.RentabilidadResponseDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.puerto.entrada.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpresaDashboardFinancieroService – Pruebas Funcionales")
class EmpresaDashboardFinancieroServiceTest {

    @Mock
    private EmpresaUseCase empresaUseCase;
    @Mock
    private ProyectoUseCase proyectoUseCase;
    @Mock
    private MetricaUseCase metricaUseCase;
    @Mock
    private IngresoUseCase ingresoUseCase;
    @Mock
    private EgresoUseCase egresoUseCase;

    @InjectMocks
    private EmpresaDashboardFinancieroService dashboardService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debe calcular correctamente rentabilidad, semáforos y márgenes")
    void obtener_calculosCorrectos() {
        // Arrange
        EmpresaResponseDto empresa = EmpresaResponseDto.builder().id(10L).nombre("Tech").build();
        ProyectoResponseDto p1 = ProyectoResponseDto.builder().id(100L).nombre("P1").build();

        RentabilidadResponseDto r1 = RentabilidadResponseDto.builder()
                .proyectoId(100L)
                .ingresoPlanificado(new BigDecimal("10000.00"))
                .costoPlanificado(new BigDecimal("5000.00"))
                .costoLaboral(new BigDecimal("2000.00"))
                .horasPlanificadas(new BigDecimal("100.00"))
                .horasReales(new BigDecimal("50.00"))
                .cpi(new BigDecimal("2.5")) // 5000 / 2000
                .spi(new BigDecimal("0.5")) // 50 / 100
                .margenReal(new BigDecimal("8000.00")) // Asumiendo ingreso real 10000
                .esRentable(true)
                .build();

        IngresoResponseDto ingreso = IngresoResponseDto.builder().monto(new BigDecimal("10000.00")).build();

        when(empresaUseCase.obtenerPorId(10L)).thenReturn(empresa);
        when(proyectoUseCase.listarActivosPorEmpresaParaUsuario(10L, 1L, "PM")).thenReturn(List.of(p1));
        when(metricaUseCase.calcularRentabilidadActual(100L)).thenReturn(r1);
        when(ingresoUseCase.listarPorEmpresa(10L)).thenReturn(List.of(ingreso));
        when(egresoUseCase.listarPorEmpresa(10L)).thenReturn(List.of());

        EmpresaDashboardFinancieroResponseDto resultado = dashboardService.obtener(10L, 1L, "PM");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalCostoLaboral()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(resultado.getSpi()).isEqualTo(new BigDecimal("0.5000")); // 50/100
        assertThat(resultado.getProyectosRentables()).isEqualTo(1);
        assertThat(resultado.getSemaforo()).isEqualTo("VERDE"); // Margen > 0, CPI > 1, SPI < 1
    }

    @Test
    @DisplayName("Debe generar semáforo ROJO si hay margen negativo o excesos de costos")
    void obtener_semaforoRojo_alertasCorrectas() {
        EmpresaResponseDto empresa = EmpresaResponseDto.builder().id(10L).nombre("Tech").build();
        ProyectoResponseDto p1 = ProyectoResponseDto.builder().id(100L).nombre("P1").build();

        RentabilidadResponseDto r1 = RentabilidadResponseDto.builder()
                .proyectoId(100L)
                .costoPlanificado(new BigDecimal("1000.00"))
                .costoLaboral(new BigDecimal("5000.00")) // Costos superan lo planificado
                .horasPlanificadas(new BigDecimal("10.0"))
                .horasReales(new BigDecimal("50.0")) // Horas superadas (SPI = 5.0)
                .build();

        when(empresaUseCase.obtenerPorId(10L)).thenReturn(empresa);
        when(proyectoUseCase.listarActivosPorEmpresaParaUsuario(10L, 1L, "PM")).thenReturn(List.of(p1));
        when(metricaUseCase.calcularRentabilidadActual(100L)).thenReturn(r1);
        when(ingresoUseCase.listarPorEmpresa(10L)).thenReturn(List.of());
        when(egresoUseCase.listarPorEmpresa(10L)).thenReturn(List.of());

        EmpresaDashboardFinancieroResponseDto resultado = dashboardService.obtener(10L, 1L, "PM");

        assertThat(resultado.getSemaforo()).isEqualTo("ROJO");
        assertThat(resultado.getAlertas()).contains(
                "La empresa tiene margen real negativo en los proyectos activos",
                "El costo real total supera el costo planificado total",
                "Las horas reales totales superan las horas planificadas",
                "Hay 1 proyecto(s) en riesgo");
    }
}
