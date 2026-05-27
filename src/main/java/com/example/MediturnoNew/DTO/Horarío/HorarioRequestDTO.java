package com.example.MediturnoNew.DTO.Horarío;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRequestDTO {

    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;

    // 1=lunes ... 7=domingo. Obligatorio si no se usa fechaEspecifica
    @Min(value = 1, message = "El día de la semana debe ser entre 1 (lunes) y 7 (domingo)")
    @Max(value = 7, message = "El día de la semana debe ser entre 1 (lunes) y 7 (domingo)")
    private Integer diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean activo = true;
}
