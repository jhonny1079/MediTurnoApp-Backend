package com.example.MediturnoNew.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;

    /** Documento Nacional de Identidad, único. */
    @Column(unique = true, nullable = false, length = 20)
    private String cedula;

    /** Fecha de nacimiento del paciente. */
    private LocalDate fechaNacimiento;

    /** seguro médico (opcional). */
    private String seguroMedico;

    /**
     * Relación uno a uno con Usuario (dueña de la clave foránea).
     * FetchType.LAZY para no cargar el usuario a menos que sea necesario.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
}
