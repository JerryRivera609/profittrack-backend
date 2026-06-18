package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.metricaDto.RentabilidadResponseDto;
import com.profitrack.aplicacion.dto.ownerDashboardDto.OwnerDashboardResponseDto;
import com.profitrack.aplicacion.dto.proyectoDto.ProyectoResponseDto;
import com.profitrack.aplicacion.dto.proyectoEmpleadoDto.ProyectoEmpleadoResponseDto;
import com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResumenDto;
import com.profitrack.aplicacion.puerto.entrada.*;
import com.profitrack.dominio.model.CostoRegistroHoras;
import com.profitrack.dominio.model.Empleado;
import com.profitrack.dominio.model.EstadoAprobacion;
import com.profitrack.dominio.model.RegistroHoras;
import com.profitrack.dominio.puerto.salida.CostoRegistroHorasRepository;
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
@DisplayName("OwnerDashboardService – Pruebas Funcionales")
class OwnerDashboardServiceTest {

        @Mock
        private ProyectoUseCase proyectoUseCase;
        @Mock
        private MetricaUseCase metricaUseCase;
        @Mock
        private RegistroHorasUseCase registroHorasUseCase;
        @Mock
        private ProyectoEmpleadoUseCase proyectoEmpleadoUseCase;
        @Mock
        private ProyectoCostoEmpleadoUseCase proyectoCostoEmpleadoUseCase;
        @Mock
        private IngresoUseCase ingresoUseCase;
        @Mock
        private EgresoUseCase egresoUseCase;
        @Mock
        private CostoRegistroHorasRepository costoRegistroRepo;

        @InjectMocks
        private OwnerDashboardService ownerDashboardService;

        @BeforeEach
        void setUp() {
        }

        @Test
        @DisplayName("Debe agregar datos correctamente y generar el dashboard de owner sin alertas rojas")
        void obtenerPorProyecto_exito_sinRiesgos() {
                ProyectoResponseDto proyecto = ProyectoResponseDto.builder().id(100L).nombre("P1").build();

                RentabilidadResponseDto rentabilidad = RentabilidadResponseDto.builder()
                                .proyectoId(100L)
                                .esRentable(true)
                                .margenReal(new BigDecimal("5000.00"))
                                .cpi(new BigDecimal("1.5"))
                                .spi(new BigDecimal("0.8"))
                                .costoReal(new BigDecimal("1000.00"))
                                .costoPlanificado(new BigDecimal("2000.00"))
                                .horasReales(new BigDecimal("40.0"))
                                .horasInvertidas(new BigDecimal("40.0"))
                                .horasPlanificadas(new BigDecimal("50.0"))
                                .ingresoReal(new BigDecimal("6000.00"))
                                .build();

                RegistroHorasResumenDto resumenHoras = RegistroHorasResumenDto.builder()
                                .totalHorasPendientes(BigDecimal.ZERO)
                                .build();

                Empleado emp = Empleado.builder().nombres("Juan").apellidos("Perez").build();
                emp.setId(1L);
                RegistroHoras rh = RegistroHoras.builder()
                                .empleado(emp)
                                .horasTrabajadas(new BigDecimal("10"))
                                .estadoAprobacion(EstadoAprobacion.APROBADO)
                                .build();
                CostoRegistroHoras costo1 = CostoRegistroHoras.builder()
                                .registroHoras(rh)
                                .costoHora(new BigDecimal("10.00"))
                                .costoTotal(new BigDecimal("100.00"))
                                .build();

                ProyectoEmpleadoResponseDto equipo1 = ProyectoEmpleadoResponseDto.builder()
                                .costoHoraCongelado(new BigDecimal("20.00"))
                                .build();

                when(proyectoUseCase.obtenerPorIdParaUsuario(100L, 1L, "ADMIN")).thenReturn(proyecto);
                when(metricaUseCase.calcularRentabilidadActual(100L)).thenReturn(rentabilidad);
                when(registroHorasUseCase.obtenerResumen(10L, 100L, null)).thenReturn(resumenHoras);
                when(costoRegistroRepo.buscarPorProyecto(100L)).thenReturn(List.of(costo1));
                when(proyectoEmpleadoUseCase.listarPorProyecto(100L)).thenReturn(List.of(equipo1));
                when(proyectoCostoEmpleadoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(ingresoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(egresoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(metricaUseCase.listarPorProyecto(100L)).thenReturn(List.of());

                OwnerDashboardResponseDto resultado = ownerDashboardService.obtenerPorProyecto(100L, 10L, 1L, "ADMIN");

                assertThat(resultado).isNotNull();
                assertThat(resultado.getSemaforo()).isEqualTo("VERDE");
                assertThat(resultado.getAlertas()).isEmpty();
                assertThat(resultado.getCostosPorEmpleado()).hasSize(1);
                assertThat(resultado.getCostosPorEmpleado().get(0).getTotalCosto()).isEqualTo(new BigDecimal("100.00"));
        }

        @Test
        @DisplayName("Debe generar alertas si hay margen negativo y horas pendientes")
        void obtenerPorProyecto_conRiesgos_generarAlertasYRojo() {
                ProyectoResponseDto proyecto = ProyectoResponseDto.builder().id(100L).nombre("P1").build();

                RentabilidadResponseDto rentabilidad = RentabilidadResponseDto.builder()
                                .proyectoId(100L)
                                .esRentable(false)
                                .margenReal(new BigDecimal("-1000.00"))
                                .cpi(new BigDecimal("0.5"))
                                .spi(new BigDecimal("1.5"))
                                .costoReal(new BigDecimal("3000.00"))
                                .costoPlanificado(new BigDecimal("2000.00"))
                                .horasReales(new BigDecimal("60.0"))
                                .horasInvertidas(new BigDecimal("60.0"))
                                .horasPlanificadas(new BigDecimal("50.0"))
                                .ingresoReal(BigDecimal.ZERO)
                                .build();

                RegistroHorasResumenDto resumenHoras = RegistroHorasResumenDto.builder()
                                .totalHorasPendientes(new BigDecimal("5.0"))
                                .build();

                ProyectoEmpleadoResponseDto equipo1 = ProyectoEmpleadoResponseDto.builder()
                                .costoHoraCongelado(BigDecimal.ZERO)
                                .build();

                when(proyectoUseCase.obtenerPorIdParaUsuario(100L, 1L, "ADMIN")).thenReturn(proyecto);
                when(metricaUseCase.calcularRentabilidadActual(100L)).thenReturn(rentabilidad);
                when(registroHorasUseCase.obtenerResumen(10L, 100L, null)).thenReturn(resumenHoras);
                when(costoRegistroRepo.buscarPorProyecto(100L)).thenReturn(List.of());
                when(proyectoEmpleadoUseCase.listarPorProyecto(100L)).thenReturn(List.of(equipo1));
                when(proyectoCostoEmpleadoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(ingresoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(egresoUseCase.listarPorProyecto(100L)).thenReturn(List.of());
                when(metricaUseCase.listarPorProyecto(100L)).thenReturn(List.of());

                OwnerDashboardResponseDto resultado = ownerDashboardService.obtenerPorProyecto(100L, 10L, 1L, "ADMIN");

                assertThat(resultado.getSemaforo()).isEqualTo("ROJO");
                assertThat(resultado.getAlertas()).contains(
                                "El proyecto esta perdiendo dinero: el margen real es negativo",
                                "El costo real supera el presupuesto planificado",
                                "Las horas reales superan las horas planificadas",
                                "Hay horas pendientes de aprobacion",
                                "Hay costos reales pero aun no hay ingresos registrados",
                                "Hay empleados asignados sin costo hora aplicado");
        }
}
