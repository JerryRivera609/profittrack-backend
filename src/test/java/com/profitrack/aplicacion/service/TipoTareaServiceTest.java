package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaPatchDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaRequestDto;
import com.profitrack.aplicacion.dto.tipoTareaDto.TipoTareaResponseDto;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.TipoTarea;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.dominio.puerto.salida.TipoTareaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoTareaServiceTest {

    @Mock
    private TipoTareaRepository tipoTareaRepo;

    @Mock
    private EmpresaRepository empresaRepo;

    @InjectMocks
    private TipoTareaService tipoTareaService;

    private Empresa empresa;
    private TipoTarea tipoTarea;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder().nombre("Emp").build();
        empresa.setId(1L);

        tipoTarea = TipoTarea.builder()
                .empresa(empresa)
                .nombre("Test")
                .descripcion("Desc")
                .build();
        tipoTarea.setId(10L);
        tipoTarea.setActivo(true);
    }

    @Test
    void crear() {
        TipoTareaRequestDto req = new TipoTareaRequestDto();
        req.setEmpresaId(1L);
        req.setNombre("Test");
        req.setDescripcion("Desc");

        when(empresaRepo.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        when(tipoTareaRepo.guardar(any(TipoTarea.class))).thenReturn(tipoTarea);

        TipoTareaResponseDto res = tipoTareaService.crear(req);
        assertThat(res.getNombre()).isEqualTo("Test");
    }

    @Test
    void obtenerPorId() {
        when(tipoTareaRepo.buscarPorId(10L)).thenReturn(Optional.of(tipoTarea));
        TipoTareaResponseDto res = tipoTareaService.obtenerPorId(10L);
        assertThat(res.getNombre()).isEqualTo("Test");
    }

    @Test
    void listarActivosPorEmpresa() {
        when(tipoTareaRepo.buscarActivosPorEmpresa(1L)).thenReturn(List.of(tipoTarea));
        List<TipoTareaResponseDto> res = tipoTareaService.listarActivosPorEmpresa(1L);
        assertThat(res).hasSize(1);
    }

    @Test
    void actualizar() {
        TipoTareaPatchDto patch = new TipoTareaPatchDto();
        patch.setNombre("Nuevo");
        patch.setDescripcion("Nueva desc");

        when(tipoTareaRepo.buscarPorId(10L)).thenReturn(Optional.of(tipoTarea));
        when(tipoTareaRepo.guardar(any(TipoTarea.class))).thenReturn(tipoTarea);

        TipoTareaResponseDto res = tipoTareaService.actualizar(10L, patch);
        assertThat(res.getNombre()).isEqualTo("Nuevo");
    }

    @Test
    void eliminar() {
        when(tipoTareaRepo.buscarPorId(10L)).thenReturn(Optional.of(tipoTarea));
        tipoTareaService.eliminar(10L);
        assertThat(tipoTarea.getActivo()).isFalse();
    }
}
