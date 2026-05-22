package com.profitrack.infraestructura.seguridad;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtils {

    public Long getEmpresaId() {
        return getJwt().getClaim("empresaId");
    }

    public Long getUserId() {
        return getJwt().getClaim("userId");
    }

    public String getRolNombre() {
        return getJwt().getClaimAsString("rolNombre");
    }

    public String getTipo() {
        return getJwt().getClaimAsString("tipo");
    }

    public String getCorreo() {
        return getJwt().getSubject();
    }

    public void validarRol(String... rolesPermitidos) {
        String rolActual = getRolNombre();
        for (String permitido : rolesPermitidos) {
            if (permitido.equalsIgnoreCase(rolActual))
                return;
        }
        if ("duenio".equalsIgnoreCase(getTipo()))
            return;

        throw new RuntimeException(
                "No tiene permisos para esta operación. Rol requerido: " +
                        String.join(" o ", rolesPermitidos));
    }

    private Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("No hay usuario autenticado en el contexto de seguridad");
        }
        return jwt;
    }
}
