package com.example.MediturnoNew.DTO.Turno;

import com.example.MediturnoNew.Enumeraciones.EstadoTurno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de SALIDA para un turno.
 * Expone solo los datos que el frontend necesita ver,
 * sin filtrar objetos completos con datos sensibles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDTO {

    private Long id;

    // --- Datos del paciente (solo lo esencial) ---
    private Long pacienteId;
    private String pacienteNombre;   // nombre + apellido del paciente
    private String pacienteCedula;

    // --- Datos del médico ---
    private Long medicoId;
    private String medicoNombre;     // nombre + apellido del médico
    private String medicoMatricula;

    // --- Datos de la especialidad ---
    private Long especialidadId;
    private String especialidadNombre;

    // --- Datos del turno en sí ---
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoTurno estado;
    private String motivoConsulta;
    private String notas;
}
