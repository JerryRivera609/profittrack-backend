package com.profitrack.dominio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "etapas_proyecto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtapaProyecto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false)
    private Integer orden;

    @Column(name = "horas_planificadas", precision = 10, scale = 2)
    private BigDecimal horasPlanificadas;

    @Column(name = "horas_reales", precision = 10, scale = 2)
    private BigDecimal horasReales;

    @Column(name = "fecha_inicio_planificada")
    private LocalDate fechaInicioPlanificada;

    @Column(name = "fecha_fin_planificada")
    private LocalDate fechaFinPlanificada;

    @Column(name = "fecha_inicio_real")
    private LocalDate fechaInicioReal;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private EstadoEtapa estado;
}
