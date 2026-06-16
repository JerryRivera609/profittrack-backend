package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoPatchDto;
import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoRequestDto;
import com.profitrack.aplicacion.dto.tareaProyectoDto.TareaProyectoResponseDto;
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
@DisplayName("TareaProyectoService – Pruebas Funcionales")
class TareaProyectoServiceTest {

    @Mock
    private TareaProyectoRepository tareaRepo;
    @Mock
    private ProyectoRepository proyectoRepo;
    @Mock
    private EtapaProyectoRepository etapaRepo;
    @Mock
    private EmpleadoRepository empleadoRepo;
    @Mock
    private TipoTareaRepository tipoTareaRepo;
    @Mock
    private RegistroHorasRepository registroHorasRepo;

    @InjectMocks
    private TareaProyectoService tareaProyectoService;

    private Proyecto proyecto;
    private EtapaProyecto etapa;
    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = Empleado.builder().nombres("Juan").apellidos("Perez").build();
        empleado.setId(1L);

        proyecto = Proyecto.builder()
                .nombre("Proyecto Prueba")
                .estado(EstadoProyecto.PLANIFICADO)
                .build();
        proyecto.setId(10L);

        etapa = EtapaProyecto.builder()
                .proyecto(proyecto)
                .estado(EstadoEtapa.PENDIENTE)
                .horasPlanificadas(new BigDecimal("50.0"))
                .build();
        etapa.setId(20L);
        etapa.setActivo(true);
    }

    // ────────────────────────────────────────────
    // CREAR TAREA
    // ────────────────────────────────────────────
    @Nested
    @DisplayName("Crear tarea")
    class CrearTarea {

        @Test
        @DisplayName("Debe crear la tarea si no excede horas de la etapa y etapa no está finalizada")
        void crear_exito() {
            TareaProyectoRequestDto dto = new TareaProyectoRequestDto();
            dto.setProyectoId(10L);
            dto.setEtapaProyectoId(20L);
            dto.setHorasPlanificadas(new BigDecimal("20.0"));
            dto.setNombre("Nueva Tarea");

            when(proyectoRepo.buscarPorId(10L)).thenReturn(Optional.of(proyecto));
            when(etapaRepo.buscarPorId(20L)).thenReturn(Optional.of(etapa));
            when(tareaRepo.buscarActivasPorEtapa(20L)).thenReturn(List.of());

            TareaProyecto guardada = TareaProyecto.builder()
                    .proyecto(proyecto).etapaProyecto(etapa)
                    .horasPlanificadas(new BigDecimal("20.0"))
                    .estado(EstadoTarea.PENDIENTE).build();
            guardada.setId(100L);

            when(tareaRepo.guardar(any(TareaProyecto.class))).thenReturn(guardada);

            TareaProyectoResponseDto resultado = tareaProyectoService.crear(dto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
            assertThat(resultado.getHorasPlanificadas()).isEqualTo(new BigDecimal("20.0"));
        }

        @Test
        @DisplayName("Debe lanzar excepción si las horas exceden a las de la etapa")
        void crear_excedeHorasEtapa_lanzaError() {
            TareaProyectoRequestDto dto = new TareaProyectoRequestDto();
            dto.setProyectoId(10L);
            dto.setEtapaProyectoId(20L);
            dto.setHorasPlanificadas(new BigDecimal("60.0")); // La etapa solo tiene 50

            when(proyectoRepo.buscarPorId(10L)).thenReturn(Optional.of(proyecto));
            when(etapaRepo.buscarPorId(20L)).thenReturn(Optional.of(etapa));
            when(tareaRepo.buscarActivasPorEtapa(20L)).thenReturn(List.of());

            assertThatThrownBy(() -> tareaProyectoService.crear(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("no puede superar las horas planificadas");
        }

        @Test
        @DisplayName("Debe lanzar excepción si la etapa ya está finalizada")
        void crear_etapaFinalizada_lanzaError() {
            etapa.setEstado(EstadoEtapa.FINALIZADA);

            TareaProyectoRequestDto dto = new TareaProyectoRequestDto();
            dto.setProyectoId(10L);
            dto.setEtapaProyectoId(20L);

            when(proyectoRepo.buscarPorId(10L)).thenReturn(Optional.of(proyecto));
            when(etapaRepo.buscarPorId(20L)).thenReturn(Optional.of(etapa));

            assertThatThrownBy(() -> tareaProyectoService.crear(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("la etapa esta FINALIZADA");
        }
    }

    // ────────────────────────────────────────────
    // ACTUALIZAR ESTADO TAREA
    // ────────────────────────────────────────────
    @Nested
    @DisplayName("Actualizar tarea")
    class ActualizarTarea {

        @Test
        @DisplayName("Lanza excepción al cambiar tarea a EN_CURSO si el proyecto no está EN_PROCESO")
        void actualizar_aEnCursoProyectoPausado_lanzaError() {
            TareaProyecto t = TareaProyecto.builder()
                    .proyecto(proyecto).etapaProyecto(etapa).estado(EstadoTarea.PENDIENTE).build();
            t.setId(100L);

            // El proyecto y etapa están en PENDIENTE o PLANIFICADO
            when(tareaRepo.buscarPorId(100L)).thenReturn(Optional.of(t));

            TareaProyectoPatchDto dto = new TareaProyectoPatchDto();
            dto.setEstado(EstadoTarea.EN_CURSO.name());

            assertThatThrownBy(() -> tareaProyectoService.actualizar(100L, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("la etapa debe estar EN_CURSO");
        }

        @Test
        @DisplayName("Permite cambiar a EN_CURSO si la etapa está EN_CURSO y proyecto EN_PROCESO")
        void actualizar_aEnCursoValido_exito() {
            proyecto.setEstado(EstadoProyecto.EN_PROCESO);
            etapa.setEstado(EstadoEtapa.EN_CURSO);

            TareaProyecto t = TareaProyecto.builder()
                    .proyecto(proyecto).etapaProyecto(etapa).estado(EstadoTarea.PENDIENTE).build();
            t.setId(100L);

            when(tareaRepo.buscarPorId(100L)).thenReturn(Optional.of(t));
            when(tareaRepo.guardar(any(TareaProyecto.class))).thenReturn(t);

            TareaProyectoPatchDto dto = new TareaProyectoPatchDto();
            dto.setEstado(EstadoTarea.EN_CURSO.name());

            TareaProyectoResponseDto resultado = tareaProyectoService.actualizar(100L, dto);

            assertThat(resultado.getEstado()).isEqualTo("EN_CURSO");
        }
    }

    // ────────────────────────────────────────────
    // ELIMINAR TAREA
    // ────────────────────────────────────────────
    @Nested
    @DisplayName("Eliminar tarea")
    class EliminarTarea {

        @Test
        @DisplayName("Debe lanzar excepción si se intenta eliminar tarea con horas aprobadas")
        void eliminar_conHorasAprobadas_lanzaError() {
            TareaProyecto t = TareaProyecto.builder().build();
            t.setId(100L);
            RegistroHoras rh = RegistroHoras.builder().estadoAprobacion(EstadoAprobacion.APROBADO).build();
            rh.setId(500L);

            when(tareaRepo.buscarPorId(100L)).thenReturn(Optional.of(t));
            when(registroHorasRepo.buscarActivosPorTarea(100L)).thenReturn(List.of(rh));

            assertThatThrownBy(() -> tareaProyectoService.eliminar(100L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No se puede eliminar una tarea con horas aprobadas");
        }
    }
}
