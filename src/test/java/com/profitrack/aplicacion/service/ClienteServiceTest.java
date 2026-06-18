package com.profitrack.aplicacion.service;

import com.profitrack.aplicacion.dto.clienteDto.ClienteRequestDto;
import com.profitrack.aplicacion.dto.clienteDto.ClienteResponseDto;
import com.profitrack.dominio.model.Cliente;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.puerto.salida.ClienteRepository;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Empresa empresa;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        empresa = Empresa.builder().nombre("Mi Empresa").build();
        empresa.setId(1L);
        empresa.setActivo(true);

        cliente = Cliente.builder()
                .empresa(empresa)
                .razonSocial("Cliente SA")
                .ruc("12345678901")
                .build();
        cliente.setId(10L);
        cliente.setActivo(true);
    }

    @Test
    void crear_exito() {
        ClienteRequestDto req = new ClienteRequestDto();
        req.setEmpresaId(1L);
        req.setRazonSocial("Cliente SA");
        req.setRuc("12345678901");

        when(empresaRepository.buscarPorId(1L)).thenReturn(Optional.of(empresa));
        when(clienteRepository.guardar(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDto res = clienteService.crear(req);
        assertNotNull(res);
        assertEquals("Cliente SA", res.getRazonSocial());
    }

    @Test
    void obtenerPorId_exito() {
        when(clienteRepository.buscarPorId(10L)).thenReturn(Optional.of(cliente));
        ClienteResponseDto res = clienteService.obtenerPorId(10L);
        assertEquals("Cliente SA", res.getRazonSocial());
    }
}
