package com.profitrack.dominio.puerto.salida;

import com.profitrack.dominio.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> buscarActivosPorEmpresa(Long empresaId);
    List<Cliente> buscarInactivosPorEmpresa(Long empresaId);
}
