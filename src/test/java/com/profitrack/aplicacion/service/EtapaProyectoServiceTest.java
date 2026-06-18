package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoRequestDto;
import com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoResponseDto;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.EtapaProyecto;
import com.profitrack.dominio.model.Proyecto;
import com.profitrack.dominio.puerto.salida.EtapaProyectoRepository;
import com.profitrack.dominio.puerto.salida.ProyectoRepository;
import com.profitrack.dominio.puerto.salida.TareaProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EtapaProyectoServiceTest {

    @Mock
    private EtapaProyectoRepository etapaRepo;

    @Mock
    private ProyectoRepository proyectoRepo;

    @Mock
    private TareaProyectoRepository tareaRepo;

    @InjectMocks
    private EtapaProyectoService etapaProyectoService;

    private Proyecto proyecto;
    private EtapaProyecto etapa;

    @BeforeEach
    void setUp() {
        Empresa e = Empresa.builder().nombre("Emp").build();
        e.setId(1L);

        proyecto = Proyecto.builder().empresa(e).nombre("Proy 1").build();
        proyecto.setId(10L);
        proyecto.setActivo(true);

        etapa = EtapaProyecto.builder()
                .proyecto(proyecto)
                .nombre("Etapa 1")
                .horasPlanificadas(new BigDecimal("10"))
                .build();
        etapa.setId(100L);
        etapa.setActivo(true);
    }

    @Test
    void crear_exito() {
        EtapaProyectoRequestDto req = new EtapaProyectoRequestDto();
        req.setProyectoId(10L);
        req.setNombre("Etapa 1");
        req.setHorasPlanificadas(new BigDecimal("10"));

        when(proyectoRepo.buscarPorId(10L)).thenReturn(Optional.of(proyecto));
        when(etapaRepo.buscarActivasPorProyecto(10L)).thenReturn(List.of());
        when(etapaRepo.guardar(any(EtapaProyecto.class))).thenReturn(etapa);

        EtapaProyectoResponseDto res = etapaProyectoService.crear(req);
        assertNotNull(res);
        assertEquals("Etapa 1", res.getNombre());
    }

    @Test
    void obtenerPorId_exito() {
        when(etapaRepo.buscarPorId(100L)).thenReturn(Optional.of(etapa));
        when(tareaRepo.buscarActivasPorEtapa(100L)).thenReturn(List.of());
        
        EtapaProyectoResponseDto res = etapaProyectoService.obtenerPorId(100L);
        assertEquals("Etapa 1", res.getNombre());
    }
    @Test
    void actualizar_exito() {
        when(etapaRepo.buscarPorId(100L)).thenReturn(Optional.of(etapa));
        when(etapaRepo.guardar(any(EtapaProyecto.class))).thenReturn(etapa);
        
        com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoPatchDto dto = new com.profitrack.aplicacion.dto.etapaProyectoDto.EtapaProyectoPatchDto();
        dto.setNombre("Modificado");
        
        EtapaProyectoResponseDto res = etapaProyectoService.actualizar(100L, dto);
        assertEquals("Modificado", res.getNombre());
    }

    @Test
    void eliminar_exito() {
        when(etapaRepo.buscarPorId(100L)).thenReturn(Optional.of(etapa));
        when(tareaRepo.buscarActivasPorEtapa(100L)).thenReturn(List.of());
        when(etapaRepo.guardar(any(EtapaProyecto.class))).thenReturn(etapa);
        
        etapaProyectoService.eliminar(100L);
        assertEquals(false, etapa.getActivo());
    }

    @Test
    void listarPorProyecto_exito() {
        when(etapaRepo.buscarActivasPorProyecto(10L)).thenReturn(List.of(etapa));
        when(tareaRepo.buscarActivasPorEtapas(any())).thenReturn(List.of());
        
        List<EtapaProyectoResponseDto> res = etapaProyectoService.listarPorProyecto(10L);
        assertEquals(1, res.size());
    }
    @Test
    void listarInactivasPorProyecto_exito() {
        when(etapaRepo.buscarInactivasPorProyecto(10L)).thenReturn(List.of(etapa));
        when(tareaRepo.buscarActivasPorEtapas(any())).thenReturn(List.of());
        
        List<EtapaProyectoResponseDto> res = etapaProyectoService.listarInactivasPorProyecto(10L);
        assertEquals(1, res.size());
    }
}
