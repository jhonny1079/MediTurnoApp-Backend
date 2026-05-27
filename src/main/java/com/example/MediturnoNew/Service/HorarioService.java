package com.example.MediturnoNew.Service;



import com.example.MediturnoNew.DTO.Horarío.HorarioRequestDTO;
import com.example.MediturnoNew.DTO.Horarío.HorarioResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface HorarioService {
    HorarioResponseDTO crearHorario(HorarioRequestDTO dto);
    HorarioResponseDTO actualizarHorario(Long id, HorarioRequestDTO dto);
    void eliminarHorario(Long id);
    HorarioResponseDTO obtenerHorarioPorId(Long id);
    List<HorarioResponseDTO> obtenerHorariosPorMedico(Long medicoId);
    List<HorarioResponseDTO> obtenerHorariosDisponibles(Long medicoId, LocalDate fecha);
}
