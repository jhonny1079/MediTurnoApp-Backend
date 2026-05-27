package com.example.MediturnoNew.Repository;


import com.example.MediturnoNew.Model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long> {

    /** Busca un paciente por su DNI. */
    Optional<Paciente> findByCedula(String cedula);
    /** Busca un paciente por el ID de su usuario asociado. */
    Optional<Paciente> findByUsuarioId(Long usuarioId);




}
