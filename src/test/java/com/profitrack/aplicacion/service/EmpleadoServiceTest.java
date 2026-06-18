package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.empleadoDto.EmpleadoRequestDto;
import com.profitrack.aplicacion.dto.empleadoDto.EmpleadoResponseDto;
import com.profitrack.dominio.model.Empleado;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.puerto.salida.EmpleadoRepository;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.infraestructura.repository.RolJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;
    @Mock
    private EmpresaRepository empresaRepository;
    @Mock
    private RolJpaRepository rolJpaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empresa empresa;
    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder().nombre("Tech").build();
        empresa.setId(10L);
        empresa.setActivo(true);

        empleado = Empleado.builder()
                .empresa(empresa)
                .nombres("Juan")
                .correo("juan@tech.com")
                .build();
        empleado.setId(100L);
        empleado.setActivo(true);
    }

    @Test
    void crear_exito() {
        EmpleadoRequestDto req = new EmpleadoRequestDto();
        req.setEmpresaId(10L);
        req.setCorreo("nuevo@tech.com");
        req.setContrasenia("1234");
        
        when(empresaRepository.buscarPorId(10L)).thenReturn(Optional.of(empresa));
        when(empleadoRepository.existePorCorreo("nuevo@tech.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("hashed");
        when(empleadoRepository.guardar(any(Empleado.class))).thenReturn(empleado);
        
        EmpleadoResponseDto res = empleadoService.crear(req);
        assertNotNull(res);
    }

    @Test
    void obtenerPorId_exito() {
        when(empleadoRepository.buscarPorId(100L)).thenReturn(Optional.of(empleado));
        EmpleadoResponseDto res = empleadoService.obtenerPorId(100L);
        assertEquals("Juan", res.getNombres());
    }

    @Test
    void listarActivosPorEmpresa_exito() {
        when(empleadoRepository.buscarActivosPorEmpresa(10L)).thenReturn(List.of(empleado));
        List<EmpleadoResponseDto> res = empleadoService.listarActivosPorEmpresa(10L);
        assertFalse(res.isEmpty());
    }

    @Test
    void eliminar_exito() {
        when(empleadoRepository.buscarPorId(100L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.guardar(any(Empleado.class))).thenReturn(empleado);
        
        empleadoService.eliminar(100L);
        verify(empleadoRepository, times(1)).guardar(empleado);
        assertFalse(empleado.getActivo());
    }
}
