package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.metricaDto.MetricaSnapshotResponseDto;
import com.profitrack.aplicacion.dto.metricaDto.RentabilidadResponseDto;
import com.profitrack.dominio.model.CostoRegistroHoras;
import com.profitrack.dominio.model.Egreso;
import com.profitrack.dominio.model.Empleado;
import com.profitrack.dominio.model.EstadoAprobacion;
import com.profitrack.dominio.model.EstadoProyecto;
import com.profitrack.dominio.model.Ingreso;
import com.profitrack.dominio.model.MetricaProyecto;
import com.profitrack.dominio.model.Proyecto;
import com.profitrack.dominio.model.RegistroHoras;
import com.profitrack.dominio.puerto.salida.CostoRegistroHorasRepository;
import com.profitrack.dominio.puerto.salida.EgresoRepository;
import com.profitrack.dominio.puerto.salida.IngresoRepository;
import com.profitrack.dominio.puerto.salida.MetricaProyectoRepository;
import com.profitrack.dominio.puerto.salida.ProyectoRepository;
import com.profitrack.dominio.puerto.salida.RegistroHorasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MetricaService - Pruebas Funcionales")
class MetricaServiceTest {

    @Mock
    private MetricaProyectoRepository metricaRepo;
    @Mock
    private ProyectoRepository proyectoRepo;
    @Mock
    private CostoRegistroHorasRepository costoRegistroRepo;
    @Mock
    private EgresoRepository egresoRepo;
    @Mock
    private IngresoRepository ingresoRepo;
    @Mock
    private RegistroHorasRepository registroHorasRepo;

    @InjectMocks
    private MetricaService metricaService;

    private Proyecto proyecto;
    private RegistroHoras horasAprobadas;
    private RegistroHoras horasPendientes;

    @BeforeEach
    void setUp() {
        proyecto = Proyecto.builder()
                .nombre("Portal web")
                .estado(EstadoProyecto.EN_PROCESO)
                .presupuestoPlanificado(bd("1000.00"))
                .precioVenta(bd("5000.00"))
                .horasPlanificadas(bd("20.00"))
                .build();
        proyecto.setId(100L);

        Empleado empleado = Empleado.builder()
                .nombres("Backend")
                .apellidos("Developer")
                .build();
        empleado.setId(1L);

        horasAprobadas = RegistroHoras.builder()
                .empleado(empleado)
                .proyecto(proyecto)
                .horasTrabajadas(bd("5.00"))
                .aprobado(true)
                .estadoAprobacion(EstadoAprobacion.APROBADO)
                .build();
        horasAprobadas.setId(10L);
        horasAprobadas.setActivo(true);

        horasPendientes = RegistroHoras.builder()
                .empleado(empleado)
                .proyecto(proyecto)
                .horasTrabajadas(bd("8.00"))
                .estadoAprobacion(EstadoAprobacion.PENDIENTE)
                .build();
        horasPendientes.setId(11L);
        horasPendientes.setActivo(true);
    }

    @Test
    @DisplayName("Debe calcular rentabilidad usando solo costos y horas aprobadas")
    void calcularRentabilidadActual_usaSoloCostosYHorasAprobadas() {
        prepararMetricasBase();

        RentabilidadResponseDto resultado = metricaService.calcularRentabilidadActual(100L);

        assertThat(resultado.getCostoLaboral()).isEqualByComparingTo("200.00");
        assertThat(resultado.getCostoOpex()).isEqualByComparingTo("50.00");
        assertThat(resultado.getCostoReal()).isEqualByComparingTo("250.00");
        assertThat(resultado.getIngresoReal()).isEqualByComparingTo("1000.00");
        assertThat(resultado.getMargenReal()).isEqualByComparingTo("750.00");
        assertThat(resultado.getHorasReales()).isEqualByComparingTo("5.00");
        assertThat(resultado.getHorasInvertidas()).isEqualByComparingTo("5.00");
        assertThat(resultado.getAvanceHorasPorcentaje()).isEqualByComparingTo("25.0000");
        assertThat(resultado.getPorcentajePresupuestoConsumido()).isEqualByComparingTo("25.0000");
        assertThat(resultado.getSaldoPresupuesto()).isEqualByComparingTo("750.00");
        assertThat(resultado.getCostoPromedioHora()).isEqualByComparingTo("40.0000");
        assertThat(resultado.getCpi()).isEqualByComparingTo("4.0000");
        assertThat(resultado.getSpi()).isEqualByComparingTo("0.2500");
        assertThat(resultado.getEsRentable()).isTrue();
    }

    @Test
    @DisplayName("Debe guardar snapshot y actualizar los totales reales del proyecto")
    void generarSnapshot_guardaMetricasYActualizaProyecto() {
        prepararMetricasBase();
        when(metricaRepo.guardar(any(MetricaProyecto.class))).thenAnswer(invocation -> {
            MetricaProyecto snapshot = invocation.getArgument(0);
            snapshot.setId(900L);
            return snapshot;
        });

        MetricaSnapshotResponseDto resultado = metricaService.generarSnapshot(100L);

        ArgumentCaptor<MetricaProyecto> snapshotCaptor = ArgumentCaptor.forClass(MetricaProyecto.class);
        verify(metricaRepo).guardar(snapshotCaptor.capture());
        verify(proyectoRepo).guardar(proyecto);

        MetricaProyecto snapshot = snapshotCaptor.getValue();
        assertThat(snapshot.getCostoReal()).isEqualByComparingTo("250.00");
        assertThat(snapshot.getIngresoReal()).isEqualByComparingTo("1000.00");
        assertThat(snapshot.getMargenReal()).isEqualByComparingTo("750.00");
        assertThat(snapshot.getHorasReales()).isEqualByComparingTo("5.00");

        assertThat(proyecto.getCostoReal()).isEqualByComparingTo("250.00");
        assertThat(proyecto.getHorasReales()).isEqualByComparingTo("5.00");
        assertThat(proyecto.getMargenReal()).isEqualByComparingTo("750.00");
        assertThat(proyecto.getMargenPlanificado()).isEqualByComparingTo("4000.00");

        assertThat(resultado.getId()).isEqualTo(900L);
        assertThat(resultado.getCostoLaboral()).isEqualByComparingTo("200.00");
        assertThat(resultado.getCostoOpex()).isEqualByComparingTo("50.00");
    }

    private void prepararMetricasBase() {
        when(proyectoRepo.buscarPorId(100L)).thenReturn(Optional.of(proyecto));
        when(costoRegistroRepo.buscarPorProyecto(100L)).thenReturn(List.of(
                CostoRegistroHoras.builder()
                        .registroHoras(horasAprobadas)
                        .costoTotal(bd("200.00"))
                        .build(),
                CostoRegistroHoras.builder()
                        .registroHoras(horasPendientes)
                        .costoTotal(bd("999.00"))
                        .build()));
        when(egresoRepo.buscarActivosPorProyecto(100L)).thenReturn(List.of(
                Egreso.builder().monto(bd("50.00")).build()));
        when(ingresoRepo.buscarActivosPorProyecto(100L)).thenReturn(List.of(
                Ingreso.builder().monto(bd("1000.00")).build()));
        when(registroHorasRepo.buscarActivosPorProyecto(100L)).thenReturn(List.of(horasAprobadas, horasPendientes));
    }

    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }
}
