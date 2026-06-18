package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.rolDto.RolRequestDto;
import com.profitrack.aplicacion.dto.rolDto.RolResponseDto;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.model.Rol;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import com.profitrack.infraestructura.repository.RolJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolJpaRepository rolRepo;

    @Mock
    private EmpresaRepository empresaRepo;

    @InjectMocks
    private RolService rolService;

    private Empresa empresa;
    private Rol rol;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder().nombre("Tech").build();
        empresa.setId(1L);
        empresa.setActivo(true);

        rol = Rol.builder()
                .empresa(empresa)
                .nombre("ADMIN")
                .build();
        rol.setId(10L);
        rol.setActivo(true);
    }

    @Test
    void crear_exito() {
        RolRequestDto req = new RolRequestDto();
        req.setEmpresaId(1L);
        req.setNombre("ADMIN");

        when(empresaRepo.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        when(rolRepo.findByEmpresaIdAndNombreIgnoreCase(1L, "ADMIN")).thenReturn(Optional.empty());
        when(rolRepo.save(any(Rol.class))).thenReturn(rol);

        RolResponseDto res = rolService.crear(req);
        assertNotNull(res);
        assertEquals("ADMIN", res.getNombre());
    }

    @Test
    void obtenerPorId_exito() {
        when(rolRepo.findById(10L)).thenReturn(Optional.of(rol));
        RolResponseDto res = rolService.obtenerPorId(10L, 1L);
        assertEquals("ADMIN", res.getNombre());
    }
}
