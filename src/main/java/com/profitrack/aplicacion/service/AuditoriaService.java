package com.profitrack.aplicacion.service;

import com.profitrack.dominio.model.Auditoria;
import com.profitrack.dominio.model.AccionAuditoria;
import com.profitrack.dominio.puerto.salida.AuditoriaRepository;
import com.profitrack.infraestructura.seguridad.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final AuditoriaRepository auditoriaRepo;
    private final SecurityContextUtils ctx;

    public void registrar(String entidad, Long entidadId, AccionAuditoria accion,
            String valoresAnteriores, String valoresNuevos) {
        auditoriaRepo.guardar(Auditoria.builder()
                .tipoUsuario(ctx.getTipo())
                .usuarioId(ctx.getUserId())
                .entidad(entidad).entidadId(entidadId)
                .accion(accion)
                .valoresAnteriores(valoresAnteriores)
                .valoresNuevos(valoresNuevos).build());
    }
}
