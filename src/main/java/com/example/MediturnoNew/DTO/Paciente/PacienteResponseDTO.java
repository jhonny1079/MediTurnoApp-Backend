package com.example.MediturnoNew.DTO.Paciente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String seguroMedico;
    // Solo exponemos datos del usuario que son seguros (nunca la contraseña)
    private Long usuarioId;
    private String email;
}
