package com.example.MediturnoNew.Repository;


import com.example.MediturnoNew.Model.HorarioDisponible;
import com.example.MediturnoNew.Model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {

    @Query("SELECT h FROM HorarioDisponible h WHERE h.medico = :medico AND h.activo = true " +
            "AND ((h.fechaEspecifica IS NULL AND h.diaSemana = :diaSemana) OR h.fechaEspecifica = :fecha)")
    List<HorarioDisponible> findActivosPorMedicoYFecha(
            @Param("medico") Medico medico,
            @Param("diaSemana") int diaSemana,
            @Param("fecha") LocalDate fecha);
}