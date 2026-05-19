package com.profitrack.dominio.puerto.salida;

import com.profitrack.dominio.model.SesionUsuario;

import java.util.Optional;

public interface SesionUsuarioRepository {
    SesionUsuario guardar(SesionUsuario sesion);
    Optional<SesionUsuario> buscarPorSessionId(String sessionId);
    Optional<SesionUsuario> buscarPorRefreshTokenHash(String hash);
    void revocarPorSessionId(String sessionId);
    void revocarTodasPorUserId(Long userId);
}
