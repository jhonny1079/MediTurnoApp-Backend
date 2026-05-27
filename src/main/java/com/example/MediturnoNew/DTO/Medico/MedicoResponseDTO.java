package com.example.MediturnoNew.DTO.Medico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private Integer duracionConsultaMinutos;
    private Long usuarioId;
    private String email;
    // Nombres de las especialidades (no objetos completos)
    private Set<String> especialidades;
}
