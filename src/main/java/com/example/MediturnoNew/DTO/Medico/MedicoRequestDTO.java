package com.example.MediturnoNew.DTO.Medico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotNull(message = "La duración de consulta es obligatoria")
    @Positive(message = "La duración debe ser un número positivo")
    private Integer duracionConsultaMinutos;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    // IDs de las especialidades que ejerce el médico
    private Set<Long> especialidadIds;
}
