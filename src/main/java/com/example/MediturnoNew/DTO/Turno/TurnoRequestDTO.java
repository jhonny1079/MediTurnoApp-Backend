package com.example.MediturnoNew.DTO.Turno;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de ENTRADA para crear un turno.
 * El frontend solo manda IDs y la fecha — el Service se encarga
 * de buscar las entidades reales y calcular horas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;

    @NotNull(message = "El ID de la especialidad es obligatorio")
    private Long especialidadId;

    @NotNull(message = "El ID del horario es obligatorio")
    private Long horarioId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "No se puede agendar un turno en el pasado")
    private LocalDate fecha;

    // Motivo opcional que escribe el paciente al sacar el turno
    private String motivoConsulta;
}
