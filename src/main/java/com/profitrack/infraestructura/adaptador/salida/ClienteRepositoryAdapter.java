package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.Cliente;
import com.profitrack.dominio.puerto.salida.ClienteRepository;
import com.profitrack.infraestructura.repository.ClienteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    @Override
    public Cliente guardar(Cliente cliente) {
        return jpaRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Cliente> buscarActivosPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaIdAndActivoTrue(empresaId);
    }

    @Override
    public List<Cliente> buscarInactivosPorEmpresa(Long empresaId) {
        return jpaRepository.findAllByEmpresaIdAndActivoFalse(empresaId);
    }
}
