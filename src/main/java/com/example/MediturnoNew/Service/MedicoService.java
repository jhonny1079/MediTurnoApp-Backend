package com.example.MediturnoNew.Service;

import com.example.MediturnoNew.DTO.Medico.MedicoRequestDTO;
import com.example.MediturnoNew.DTO.Medico.MedicoResponseDTO;

import java.util.List;

public interface MedicoService {
    MedicoResponseDTO crearMedico(MedicoRequestDTO dto);
    MedicoResponseDTO actualizarMedico(Long id, MedicoRequestDTO dto);
    void eliminarMedico(Long id);
    MedicoResponseDTO obtenerMedicoPorId(Long id);
    List<MedicoResponseDTO> obtenerMedicos();
    List<MedicoResponseDTO> obtenerMedicosPorEspecialidad(Long especialidadId);
}
