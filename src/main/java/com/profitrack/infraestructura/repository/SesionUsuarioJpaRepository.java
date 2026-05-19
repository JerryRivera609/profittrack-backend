package com.profitrack.infraestructura.repository;

import com.profitrack.dominio.model.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SesionUsuarioJpaRepository extends JpaRepository<SesionUsuario, UUID> {

    Optional<SesionUsuario> findBySessionId(String sessionId);

    Optional<SesionUsuario> findByRefreshTokenHash(String hash);

    @Modifying
    @Query("UPDATE SesionUsuario s SET s.revoked = true WHERE s.sessionId = :sessionId")
    void revokeBySessionId(@Param("sessionId") String sessionId);

    @Modifying
    @Query("UPDATE SesionUsuario s SET s.revoked = true WHERE s.userId = :userId")
    void revokeAllByUserId(@Param("userId") Long userId);
}
