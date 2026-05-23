package com.profitrack.infraestructura.seguridad;

import com.profitrack.dominio.model.SesionUsuario;
import com.profitrack.dominio.puerto.salida.SesionUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final SesionUsuarioRepository sesionRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        String token = extractFromCookie(req, "access_token");
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }

        try {
            Jwt jwt = tokenService.decode(token);
            String sessionId = jwt.getClaimAsString("sessionId");

            // aseguramos q no hayan cerrado la sesion a la fuerza
            SesionUsuario sesion = sesionRepo.buscarPorSessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

            if (!sesion.isRevoked()) {
                // capturamors el rol
                String rolNombre = jwt.getClaimAsString("rolNombre");
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + rolNombre));

                var auth = new UsernamePasswordAuthenticationToken(jwt, null, authorities);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // si el token ya murio o es trucho, lo ignoramos
            logger.info("Token inválido o expirado: " + e.getMessage());
        }

        chain.doFilter(req, res);
    }

    private String extractFromCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null)
            return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
    }
}
