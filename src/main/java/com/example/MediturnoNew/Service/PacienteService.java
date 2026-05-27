package com.example.MediturnoNew.Service;


import com.example.MediturnoNew.DTO.Paciente.PacienteRequestDTO;
import com.example.MediturnoNew.DTO.Paciente.PacienteResponseDTO;

import java.util.List;

public interface PacienteService {
    PacienteResponseDTO crearPaciente(PacienteRequestDTO dto);
    PacienteResponseDTO actualizarPaciente(Long id, PacienteRequestDTO dto);
    void eliminarPaciente(Long id);
    PacienteResponseDTO obtenerPacientePorId(Long id);
    PacienteResponseDTO obtenerPacientePorCedula(String cedula);
    List<PacienteResponseDTO> obtenerPacientes();
    PacienteResponseDTO obtenerPacientePorNombreUsuario(String nombreUsuario);

}