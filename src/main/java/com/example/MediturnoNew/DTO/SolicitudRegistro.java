package com.example.MediturnoNew.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudRegistro {

    @NotBlank @Size(min = 3, max = 50)
    private String nombreUsuario;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 100)
    private String contrasena;

    @NotBlank private String nombre;
    @NotBlank private String apellido;
    @NotBlank private String cedula;
    private String seguroMedico;
}
