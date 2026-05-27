package com.example.MediturnoNew.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_disponibles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioDisponible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Día de la semana en formato ISO (1 = lunes, ..., 7 = domingo).
     * Se usa cuando el horario es recurrente.
     */
    @Column(name = "dia_semana")
    private Integer diaSemana;

    /**
     * Fecha específica opcional. Si está presente, este horario aplica
     * únicamente para esa fecha (ignorando el día de la semana).
     */
    @Column(name = "fecha_especifica")
    private LocalTime fechaEspecifica;

    /** Hora de inicio del bloque de atención. */
    @Column(name = "hora_inicio",nullable = false)
    private LocalTime horaInicio;

    /** Hora de finalización del bloque. */
    @Column(name = "hora_fin",nullable = false)
    private LocalTime horaFin;
    /** Indica si este horario está activo (true) o desactivado temporalmente. */
    @Builder.Default
    private Boolean activo = true;

    /** Médico al que pertenece este horario. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
}
