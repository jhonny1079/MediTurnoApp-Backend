package com.example.MediturnoNew.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "especialidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre único de la especialidad. */
    @Column(unique = true, nullable = false)
    private String nombre;

    /** Descripción breve de la especialidad. */
    private String descripcion;

}