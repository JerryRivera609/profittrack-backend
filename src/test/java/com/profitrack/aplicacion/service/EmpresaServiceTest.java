package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.empresaDto.EmpresaPatchDto;
import com.profitrack.aplicacion.dto.empresaDto.EmpresaRequestDto;
import com.profitrack.aplicacion.dto.empresaDto.EmpresaResponseDto;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder()
                .nombre("Tech")
                .ruc("12345678901")
                .direccion("Lima")
                .telefono("999999999")
                .correo("tech@tech.com")
                .build();
        empresa.setId(1L);
        empresa.setActivo(true);
    }

    @Test
    void crear_exito() {
        EmpresaRequestDto req = new EmpresaRequestDto();
        req.setNombre("Tech");
        
        when(empresaRepository.guardar(any(Empresa.class))).thenReturn(empresa);
        
        EmpresaResponseDto res = empresaService.crear(req);
        
        assertNotNull(res);
        assertEquals("Tech", res.getNombre());
    }

    @Test
    void obtenerPorId_exito() {
        when(empresaRepository.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        EmpresaResponseDto res = empresaService.obtenerPorId(1L);
        assertEquals("Tech", res.getNombre());
    }

    @Test
    void obtenerPorId_noEncontrado() {
        when(empresaRepository.buscarPorId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> empresaService.obtenerPorId(1L));
    }

    @Test
    void listarActivos_exito() {
        when(empresaRepository.buscarActivos()).thenReturn(List.of(empresa));
        List<EmpresaResponseDto> res = empresaService.listarActivos();
        assertFalse(res.isEmpty());
    }

    @Test
    void actualizar_exito() {
        EmpresaPatchDto patch = new EmpresaPatchDto();
        patch.setNombre("Tech Updated");
        
        when(empresaRepository.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.guardar(any(Empresa.class))).thenReturn(empresa);
        
        EmpresaResponseDto res = empresaService.actualizar(1L, patch);
        assertNotNull(res);
    }

    @Test
    void eliminar_exito() {
        when(empresaRepository.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.guardar(any(Empresa.class))).thenReturn(empresa);
        
        empresaService.eliminar(1L);
        verify(empresaRepository, times(1)).guardar(empresa);
        assertFalse(empresa.getActivo());
    }
}
