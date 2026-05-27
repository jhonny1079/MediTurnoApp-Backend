package com.example.MediturnoNew.Repository;


import com.example.MediturnoNew.Model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    /** Verifica si ya existe una especialidad con ese nombre. */
    boolean existsByNombre(String nombre);
}
