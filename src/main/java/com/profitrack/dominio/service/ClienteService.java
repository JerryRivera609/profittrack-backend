package com.profitrack.dominio.service;

import com.profitrack.aplicacion.dto.clienteDto.ClientePatchDto;
import com.profitrack.aplicacion.dto.clienteDto.ClienteRequestDto;
import com.profitrack.aplicacion.dto.clienteDto.ClienteResponseDto;
import com.profitrack.dominio.model.Cliente;
import com.profitrack.dominio.model.Empresa;
import com.profitrack.dominio.puerto.entrada.ClienteUseCase;
import com.profitrack.dominio.puerto.salida.ClienteRepository;
import com.profitrack.dominio.puerto.salida.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService implements ClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;

    @Override
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        Empresa empresa = empresaRepository.buscarPorId(dto.getEmpresaId())
                .filter(Empresa::getActivo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + dto.getEmpresaId()));

        Cliente cliente = Cliente.builder()
                .empresa(empresa)
                .razonSocial(dto.getRazonSocial())
                .ruc(dto.getRuc())
                .nombreContacto(dto.getNombreContacto())
                .correoContacto(dto.getCorreoContacto())
                .telefonoContacto(dto.getTelefonoContacto())
                .direccion(dto.getDireccion())
                .build();

        return toDto(clienteRepository.guardar(cliente));
    }

    @Override
    public ClienteResponseDto obtenerPorId(Long id) {
        return clienteRepository.buscarPorId(id)
                .filter(Cliente::getActivo)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    @Override
    public List<ClienteResponseDto> listarActivosPorEmpresa(Long empresaId) {
        return clienteRepository.buscarActivosPorEmpresa(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDto> listarInactivosPorEmpresa(Long empresaId) {
        return clienteRepository.buscarInactivosPorEmpresa(empresaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDto reactivar(Long id) {
        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        cliente.setActivo(true);
        return toDto(clienteRepository.guardar(cliente));
    }

    @Override
    public ClienteResponseDto actualizar(Long id, ClientePatchDto dto) {
        Cliente cliente = clienteRepository.buscarPorId(id)
                .filter(Cliente::getActivo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        if (dto.getRazonSocial() != null)
            cliente.setRazonSocial(dto.getRazonSocial());
        if (dto.getRuc() != null)
            cliente.setRuc(dto.getRuc());
        if (dto.getNombreContacto() != null)
            cliente.setNombreContacto(dto.getNombreContacto());
        if (dto.getCorreoContacto() != null)
            cliente.setCorreoContacto(dto.getCorreoContacto());
        if (dto.getTelefonoContacto() != null)
            cliente.setTelefonoContacto(dto.getTelefonoContacto());
        if (dto.getDireccion() != null)
            cliente.setDireccion(dto.getDireccion());

        return toDto(clienteRepository.guardar(cliente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.buscarPorId(id)
                .filter(Cliente::getActivo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setActivo(false);
        clienteRepository.guardar(cliente);
    }

    private ClienteResponseDto toDto(Cliente c) {
        return ClienteResponseDto.builder()
                .id(c.getId())
                .empresaId(c.getEmpresa().getId())
                .razonSocial(c.getRazonSocial())
                .ruc(c.getRuc())
                .nombreContacto(c.getNombreContacto())
                .correoContacto(c.getCorreoContacto())
                .telefonoContacto(c.getTelefonoContacto())
                .direccion(c.getDireccion())
                .activo(c.getActivo())
                .build();
    }
}
