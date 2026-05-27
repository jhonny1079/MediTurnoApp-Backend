package com.example.MediturnoNew.Service;


import com.example.MediturnoNew.DTO.Turno.TurnoRequestDTO;
import com.example.MediturnoNew.DTO.Turno.TurnoResponseDTO;
import com.example.MediturnoNew.Enumeraciones.EstadoTurno;

import java.time.LocalDate;
import java.util.List;

/*
 Contrato del servicio de Turnos.
 que puede hacer el sistema con los turnos.
 El como queda en la implementación (TurnoServiceImpl).
 */
public interface TurnoService {

    /** Agendamiento de un nuevo turno */
    TurnoResponseDTO crearTurno(TurnoRequestDTO dto);

    /** Consultas */
    TurnoResponseDTO obtenerTurnoPorId(Long id);
    List<TurnoResponseDTO> obtenerTurnosPorPaciente(Long pacienteId);
    List<TurnoResponseDTO> obtenerTurnosPorMedicoYFecha(Long medicoId, LocalDate fecha);
    List<TurnoResponseDTO> obtenerTodos();

    /** Cambio de estado (CONFIRMADO, EN_CURSO, ATENDIDO, etc.) */
    TurnoResponseDTO cambiarEstado(Long id, EstadoTurno nuevoEstado);

    /** Cancelación del turno */
    void cancelarTurno(Long id);
}