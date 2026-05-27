package com.example.MediturnoNew.Repository;

import com.example.MediturnoNew.Model.Medico;
import com.example.MediturnoNew.Model.Paciente;
import com.example.MediturnoNew.Model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByPacienteOrderByFechaDescHoraInicioDesc(Paciente paciente);

    List<Turno> findByMedicoAndFecha(Medico medico, LocalDate fecha);

    @Query("SELECT COUNT(t) FROM Turno t WHERE t.medico = :medico AND t.fecha = :fecha " +
            "AND t.estado NOT IN ('CANCELADO', 'NO_ASISTIO') " +
            "AND t.horaInicio < :horaFin AND t.horaFin > :horaInicio")
    long contarSolapados(@Param("medico") Medico medico,
                         @Param("fecha") LocalDate fecha,
                         @Param("horaInicio") LocalTime horaInicio,
                         @Param("horaFin") LocalTime horaFin);
}