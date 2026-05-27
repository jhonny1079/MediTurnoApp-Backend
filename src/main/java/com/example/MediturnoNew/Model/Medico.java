package com.example.MediturnoNew.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name  = "medicos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;

    /** Número de matrícula profesional, único. */
    @Column(unique = true,  nullable = false, length = 30)
    private String matricula;

    /** Duración promedio de consulta en minutos (útil para calcular horarios). */
    private Integer duracionConsultaMinutos;

    /** Relación uno a uno con Usuario (dueña de la FK). */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    /**
     * Relación muchos a muchos con Especialidad.
     * Se usa una tabla intermedia 'medico_especialidad' con las columnas
     * medico_id y especialidad_id.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "medico_especialidad",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    @Builder.Default
    private Set<Especialidad> especialidades = new HashSet<>();
}
