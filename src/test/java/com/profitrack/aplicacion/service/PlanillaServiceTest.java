package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.historialCostoHoraDto.HistorialCostoHoraRequestDto;
import com.profitrack.aplicacion.dto.planillaDto.PlanillaRequestDto;
import com.profitrack.aplicacion.dto.planillaDto.PlanillaResponseDto;
import com.profitrack.aplicacion.puerto.entrada.HistorialCostoHoraUseCase;
import com.profitrack.dominio.model.DetallePlanilla;
import com.profitrack.dominio.model.Empleado;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.Planilla;
import com.profitrack.dominio.puerto.salida.EmpleadoRepository;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.dominio.puerto.salida.PlanillaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlanillaService - Pruebas Funcionales")
class PlanillaServiceTest {

    @Mock
    private PlanillaRepository planillaRepo;
    @Mock
    private EmpresaRepository empresaRepo;
    @Mock
    private EmpleadoRepository empleadoRepo;
    @Mock
    private HistorialCostoHoraUseCase historialCostoHoraUseCase;

    @InjectMocks
    private PlanillaService planillaService;

    private Empresa empresa;
    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder().nombre("Tech").build();
        empresa.setId(10L);

        empleado = Empleado.builder()
                .nombres("Backend")
                .apellidos("Developer")
                .build();
        empleado.setId(1L);
    }

    @Test
    @DisplayName("Debe crear planilla y registrar costo hora desde sueldo base")
    void crear_registraCostoHoraDesdeSueldoBase() {
        PlanillaRequestDto dto = requestConDetalle("2400.00", "300.00", "100.00");
        when(empresaRepo.buscarPorId(10L)).thenReturn(Optional.of(empresa));
        when(empleadoRepo.buscarPorId(1L)).thenReturn(Optional.of(empleado));
        when(planillaRepo.guardar(any(Planilla.class))).thenAnswer(invocation -> {
            Planilla planilla = invocation.getArgument(0);
            if (planilla.getId() == null) {
                planilla.setId(500L);
            }
            planilla.setActivo(true);
            return planilla;
        });
        when(planillaRepo.guardarDetalle(any(DetallePlanilla.class))).thenAnswer(invocation -> {
            DetallePlanilla detalle = invocation.getArgument(0);
            detalle.setId(700L);
            return detalle;
        });

        PlanillaResponseDto resultado = planillaService.crear(dto);

        assertThat(resultado.getMontoTotal()).isEqualByComparingTo("2600.00");
        assertThat(resultado.getDetalles()).hasSize(1);
        assertThat(resultado.getDetalles().get(0).getSueldoFinal()).isEqualByComparingTo("2600.00");

        ArgumentCaptor<HistorialCostoHoraRequestDto> costoCaptor =
                ArgumentCaptor.forClass(HistorialCostoHoraRequestDto.class);
        verify(historialCostoHoraUseCase).registrarCosto(costoCaptor.capture());
        assertThat(costoCaptor.getValue().getEmpleadoId()).isEqualTo(1L);
        assertThat(costoCaptor.getValue().getCostoHora()).isEqualByComparingTo("15.00");
        assertThat(costoCaptor.getValue().getFechaInicio()).isEqualTo(LocalDate.of(2026, 6, 1));
    }

    @Test
    @DisplayName("No debe registrar costo hora si el sueldo base es cero")
    void crear_sueldoBaseCero_noRegistraCostoHora() {
        PlanillaRequestDto dto = requestConDetalle("0.00", "50.00", "0.00");
        when(empresaRepo.buscarPorId(10L)).thenReturn(Optional.of(empresa));
        when(empleadoRepo.buscarPorId(1L)).thenReturn(Optional.of(empleado));
        when(planillaRepo.guardar(any(Planilla.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(planillaRepo.guardarDetalle(any(DetallePlanilla.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlanillaResponseDto resultado = planillaService.crear(dto);

        assertThat(resultado.getMontoTotal()).isEqualByComparingTo("50.00");
        verify(historialCostoHoraUseCase, never()).registrarCosto(any(HistorialCostoHoraRequestDto.class));
    }

    private PlanillaRequestDto requestConDetalle(String sueldoBase, String bonos, String descuentos) {
        PlanillaRequestDto dto = new PlanillaRequestDto();
        dto.setEmpresaId(10L);
        dto.setAnio(2026);
        dto.setMes(6);

        PlanillaRequestDto.DetallePlanillaDto detalle = new PlanillaRequestDto.DetallePlanillaDto();
        detalle.setEmpleadoId(1L);
        detalle.setSueldoBase(new BigDecimal(sueldoBase));
        detalle.setBonos(new BigDecimal(bonos));
        detalle.setDescuentos(new BigDecimal(descuentos));
        dto.setDetalles(List.of(detalle));

        return dto;
    }
}
