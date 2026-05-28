package com.example.MediturnoNew.Model;

import com.example.MediturnoNew.Enumeraciones.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único, utilizado para el login. */
    @Column(unique = true, nullable = false, length = 50)
    private String nombreUsuario;

    /** Correo electrónico único. */
    @Column(unique = true, nullable = false,  length = 100)
    private String email;
    /** Contraseña almacenada de forma segura (hash BCrypt). */
    @Column(nullable = false)
    private String contrasena;

    /** Rol del usuario, determina los permisos dentro de la aplicación. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    /** Indica si la cuenta está habilitada (true) o deshabilitada (false). */
    @Builder.Default
    private Boolean activo = true;

    /** Fecha y hora en que se creó la cuenta. */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // RELACIONES

    /**
     * Relación uno a uno con Paciente.
     * 'mappedBy' indica que la entidad Paciente es la dueña de la clave foránea.
     * Al guardar un Usuario, también se persiste el Paciente asociado (si existe).
     */
    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Paciente paciente;

    /**
     * Relación uno a uno con Médico.
     */
    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Medico medico;
    /**
     * Método que se ejecuta automáticamente antes de insertar por primera vez.
     * Establece la fecha de creación.
     */
    @PrePersist
    protected void alClear(){
        this.fechaCreacion = LocalDateTime.now();
    }



}
