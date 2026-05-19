package com.profitrack.infraestructura.adaptador.salida;

import com.profitrack.dominio.model.SesionUsuario;
import com.profitrack.dominio.puerto.salida.SesionUsuarioRepository;
import com.profitrack.infraestructura.repository.SesionUsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SesionUsuarioRepositoryAdapter implements SesionUsuarioRepository {

    private final SesionUsuarioJpaRepository jpaRepository;

    @Override
    public SesionUsuario guardar(SesionUsuario sesion) {
        return jpaRepository.save(sesion);
    }

    @Override
    public Optional<SesionUsuario> buscarPorSessionId(String sessionId) {
        return jpaRepository.findBySessionId(sessionId);
    }

    @Override
    public Optional<SesionUsuario> buscarPorRefreshTokenHash(String hash) {
        return jpaRepository.findByRefreshTokenHash(hash);
    }

    @Override
    public void revocarPorSessionId(String sessionId) {
        jpaRepository.revokeBySessionId(sessionId);
    }

    @Override
    public void revocarTodasPorUserId(Long userId) {
        jpaRepository.revokeAllByUserId(userId);
    }
}
