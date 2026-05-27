package com.example.MediturnoNew.DTO.Especialidad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadRequestDTO {

    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    private String nombre;

    private String descripcion;
}