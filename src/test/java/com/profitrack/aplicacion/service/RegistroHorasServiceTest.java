package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasRequestDto;
import com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResponseDto;
import com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResumenDto;
import com.profitrack.dominio.model.*;
import com.profitrack.dominio.puerto.salida.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistroHorasService – Pruebas Funcionales")
class RegistroHorasServiceTest {

        @Mock
        private RegistroHorasRepository rhRepo;
        @Mock
        private ProyectoRepository proyectoRepo;
        @Mock
        private EmpleadoRepository empleadoRepo;
        @Mock
        private ProyectoCostoEmpleadoRepository costoEmpRepo;
        @Mock
        private CostoRegistroHorasRepository costoRhRepo;
        @Mock
        private TareaProyectoRepository tareaRepo;

        @InjectMocks
        private RegistroHorasService registroHorasService;

        private Empleado empleado;
        private Proyecto proyecto;
        private EtapaProyecto etapa;
        private TareaProyecto tarea;

        @BeforeEach
        void setUp() {
                empleado = Empleado.builder().nombres("Juan").apellidos("Perez").build();
                empleado.setId(1L);

                proyecto = Proyecto.builder()
                                .nombre("Proyecto Prueba")
                                .estado(EstadoProyecto.EN_PROCESO)
                                .build();
                proyecto.setId(100L);

                etapa = EtapaProyecto.builder()
                                .proyecto(proyecto)
                                .estado(EstadoEtapa.EN_CURSO)
                                .build();
                etapa.setId(200L);

                tarea = TareaProyecto.builder()
                                .proyecto(proyecto)
                                .etapaProyecto(etapa)
                                .nombre("Desarrollo API")
                                .estado(EstadoTarea.EN_CURSO)
                                .horasReales(BigDecimal.ZERO)
                                .build();
                tarea.setId(300L);
                tarea.setActivo(true);
        }

        // ────────────────────────────────────────────
        // REGISTRAR HORAS
        // ────────────────────────────────────────────
        @Nested
        @DisplayName("Registrar horas")
        class RegistrarHoras {

                @Test
                @DisplayName("Debe registrar horas exitosamente si la tarea, etapa y proyecto están EN_CURSO")
                void registrar_exito() {
                        RegistroHorasRequestDto dto = new RegistroHorasRequestDto();
                        dto.setProyectoId(100L);
                        dto.setTareaId(300L);
                        dto.setHorasTrabajadas(new BigDecimal("4.5"));
                        dto.setDescripcion("Avance en API");

                        when(empleadoRepo.buscarPorId(1L)).thenReturn(Optional.of(empleado));
                        when(proyectoRepo.buscarPorId(100L)).thenReturn(Optional.of(proyecto));
                        when(tareaRepo.buscarPorId(300L)).thenReturn(Optional.of(tarea));

                        RegistroHoras guardado = RegistroHoras.builder()
                                        .empleado(empleado).proyecto(proyecto).tarea(tarea)
                                        .horasTrabajadas(new BigDecimal("4.5"))
                                        .estadoAprobacion(EstadoAprobacion.PENDIENTE)
                                        .build();
                        guardado.setId(500L);

                        when(rhRepo.guardar(any(RegistroHoras.class))).thenReturn(guardado);

                        RegistroHorasResponseDto resultado = registroHorasService.registrar(1L, dto);

                        assertThat(resultado).isNotNull();
                        assertThat(resultado.getEstadoAprobacion()).isEqualTo("PENDIENTE");
                        assertThat(resultado.getHorasTrabajadas()).isEqualTo(new BigDecimal("4.5"));
                }

                @Test
                @DisplayName("Debe lanzar excepción si la tarea NO está EN_CURSO")
                void registrar_tareaPendiente_lanzaError() {
                        tarea.setEstado(EstadoTarea.PENDIENTE);

                        RegistroHorasRequestDto dto = new RegistroHorasRequestDto();
                        dto.setProyectoId(100L);
                        dto.setTareaId(300L);

                        when(empleadoRepo.buscarPorId(1L)).thenReturn(Optional.of(empleado));
                        when(proyectoRepo.buscarPorId(100L)).thenReturn(Optional.of(proyecto));
                        when(tareaRepo.buscarPorId(300L)).thenReturn(Optional.of(tarea));

                        assertThatThrownBy(() -> registroHorasService.registrar(1L, dto))
                                        .isInstanceOf(IllegalArgumentException.class)
                                        .hasMessageContaining("la tarea debe estar EN_CURSO");
                }

                @Test
                @DisplayName("Debe lanzar excepción si el proyecto NO está EN_PROCESO")
                void registrar_proyectoPausado_lanzaError() {
                        proyecto.setEstado(EstadoProyecto.PAUSADO);

                        RegistroHorasRequestDto dto = new RegistroHorasRequestDto();
                        dto.setProyectoId(100L);
                        dto.setTareaId(300L);

                        when(empleadoRepo.buscarPorId(1L)).thenReturn(Optional.of(empleado));
                        when(proyectoRepo.buscarPorId(100L)).thenReturn(Optional.of(proyecto));
                        when(tareaRepo.buscarPorId(300L)).thenReturn(Optional.of(tarea));

                        assertThatThrownBy(() -> registroHorasService.registrar(1L, dto))
                                        .isInstanceOf(IllegalArgumentException.class)
                                        .hasMessageContaining("el proyecto debe estar EN_PROCESO");
                }
        }

        // ────────────────────────────────────────────
        // APROBAR HORAS
        // ────────────────────────────────────────────
        @Nested
        @DisplayName("Aprobar horas")
        class AprobarHoras {

                @Test
                @DisplayName("Debe aprobar horas, sumar a la tarea y calcular el costo")
                void aprobar_exito() {
                        RegistroHoras rh = RegistroHoras.builder()
                                        .empleado(empleado).proyecto(proyecto).tarea(tarea)
                                        .horasTrabajadas(new BigDecimal("5.0"))
                                        .estadoAprobacion(EstadoAprobacion.PENDIENTE)
                                        .build();
                        rh.setId(500L);

                        ProyectoCostoEmpleado costoHoraEmp = ProyectoCostoEmpleado.builder()
                                        .costoHora(new BigDecimal("10.00")).build();

                        when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));
                        when(costoEmpRepo.buscarActivoPorProyectoYEmpleado(100L, 1L))
                                        .thenReturn(Optional.of(costoHoraEmp));
                        when(rhRepo.buscarActivosPorTarea(300L)).thenReturn(List.of(rh));

                        RegistroHorasResponseDto resultado = registroHorasService.aprobar(500L);

                        assertThat(resultado.getEstadoAprobacion()).isEqualTo("APROBADO");
                        assertThat(tarea.getHorasReales()).isEqualTo(new BigDecimal("5.0"));

                        verify(costoRhRepo, times(1)).eliminarPorRegistroHoras(500L);
                        verify(costoRhRepo, times(1)).guardar(any(CostoRegistroHoras.class));
                        verify(tareaRepo, times(1)).guardar(tarea);
                }

                @Test
                @DisplayName("Lanza excepción si se intenta aprobar un registro ya desaprobado")
                void aprobar_yaDesaprobado_lanzaError() {
                        RegistroHoras rh = RegistroHoras.builder()
                                        .estadoAprobacion(EstadoAprobacion.DESAPROBADO)
                                        .build();
                        rh.setId(500L);

                        when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));

                        assertThatThrownBy(() -> registroHorasService.aprobar(500L))
                                        .isInstanceOf(IllegalArgumentException.class)
                                        .hasMessageContaining("No se puede aprobar un registro desaprobado");
                }
        }

        // ────────────────────────────────────────────
        // RECHAZAR HORAS
        // ────────────────────────────────────────────
        @Nested
        @DisplayName("Rechazar horas")
        class RechazarHoras {

                @Test
                @DisplayName("Debe cambiar el estado a DESAPROBADO y eliminar el costo asociado")
                void rechazar_exito() {
                        RegistroHoras rh = RegistroHoras.builder()
                                        .empleado(empleado).proyecto(proyecto)
                                        .estadoAprobacion(EstadoAprobacion.PENDIENTE)
                                        .build();
                        rh.setId(500L);

                        when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));
                        when(rhRepo.guardar(any(RegistroHoras.class))).thenReturn(rh);

                        RegistroHorasResponseDto resultado = registroHorasService.rechazar(500L);

                        assertThat(resultado.getEstadoAprobacion()).isEqualTo("DESAPROBADO");
                        verify(costoRhRepo, times(1)).eliminarPorRegistroHoras(500L);
                }
        }

        // ────────────────────────────────────────────
        // RESUMEN FINANCIERO
        // ────────────────────────────────────────────
        @Nested
        @DisplayName("Obtener resumen de horas")
        class ObtenerResumen {

                @Test
                @DisplayName("Debe calcular correctamente el total de horas agrupadas")
                void resumen_calculosCorrectos() {
                        RegistroHoras rh1 = RegistroHoras.builder()
                                        .proyecto(proyecto).empleado(empleado)
                                        .horasTrabajadas(new BigDecimal("4.0"))
                                        .estadoAprobacion(EstadoAprobacion.APROBADO).build();

                        RegistroHoras rh2 = RegistroHoras.builder()
                                        .proyecto(proyecto).empleado(empleado)
                                        .horasTrabajadas(new BigDecimal("2.5"))
                                        .estadoAprobacion(EstadoAprobacion.PENDIENTE).build();

                        when(rhRepo.buscarActivosPorEmpresa(10L)).thenReturn(List.of(rh1, rh2));

                        RegistroHorasResumenDto resumen = registroHorasService.obtenerResumen(10L, null, null);

                        assertThat(resumen.getTotalHorasRegistradas()).isEqualTo(new BigDecimal("6.5"));
                        assertThat(resumen.getTotalHorasAprobadas()).isEqualTo(new BigDecimal("4.0"));
                        assertThat(resumen.getTotalHorasPendientes()).isEqualTo(new BigDecimal("2.5"));
                }
        }

    // ────────────────────────────────────────────
    // OBTENER Y ELIMINAR
    // ────────────────────────────────────────────
    @Nested
    @DisplayName("Obtener y eliminar horas")
    class ObtenerYEliminar {
        @Test
        @DisplayName("Debe obtener registro por id")
        void obtener_exito() {
            RegistroHoras rh = RegistroHoras.builder().empleado(empleado).proyecto(proyecto).tarea(tarea).estadoAprobacion(EstadoAprobacion.PENDIENTE).build();
            rh.setId(500L);
            when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));
            
            RegistroHorasResponseDto res = registroHorasService.obtenerPorId(500L);
            assertThat(res.getId()).isEqualTo(500L);
        }
        
        @Test
        @DisplayName("Debe eliminar registro si esta PENDIENTE")
        void eliminar_exito() {
            RegistroHoras rh = RegistroHoras.builder().empleado(empleado).proyecto(proyecto).tarea(tarea).estadoAprobacion(EstadoAprobacion.PENDIENTE).build();
            rh.setId(500L);
            rh.setActivo(true);
            when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));
            when(rhRepo.guardar(any(RegistroHoras.class))).thenReturn(rh);
            
            registroHorasService.eliminar(500L);
            assertThat(rh.getActivo()).isFalse();
        }
        @Test
        void rechazar_exito() {
            RegistroHoras rh = RegistroHoras.builder().empleado(empleado).proyecto(proyecto).tarea(tarea).estadoAprobacion(EstadoAprobacion.PENDIENTE).build();
            rh.setId(500L);
            when(rhRepo.buscarPorId(500L)).thenReturn(Optional.of(rh));
            when(rhRepo.guardar(any(RegistroHoras.class))).thenReturn(rh);
            
            com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResponseDto res = registroHorasService.rechazar(500L);
            assertThat(res.getId()).isEqualTo(500L);
        }

        @Test
        void listar_exito() {
            RegistroHoras rh = RegistroHoras.builder().empleado(empleado).proyecto(proyecto).tarea(tarea).estadoAprobacion(EstadoAprobacion.PENDIENTE).build();
            rh.setId(500L);
            when(rhRepo.buscarActivosPorProyecto(10L)).thenReturn(List.of(rh));
            when(rhRepo.buscarActivosPorEmpleado(1L)).thenReturn(List.of(rh));
            
            List<com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResponseDto> res1 = registroHorasService.listarPorProyecto(10L);
            assertThat(res1).isNotEmpty();
            List<com.profitrack.aplicacion.dto.registroHorasDto.RegistroHorasResponseDto> res2 = registroHorasService.listarPorEmpleado(1L);
            assertThat(res2).isNotEmpty();
        }
    }
}
