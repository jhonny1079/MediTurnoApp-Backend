package com.example.MediturnoNew.Service;


import com.example.MediturnoNew.DTO.Especialidad.EspecialidadRequestDTO;
import com.example.MediturnoNew.DTO.Especialidad.EspecialidadResponseDTO;

import java.util.List;

public interface EspecialidadService {
    EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO dto);
    EspecialidadResponseDTO actualizarEspecialidad(Long id, EspecialidadRequestDTO dto);
    void eliminarEspecialidad(Long id);
    EspecialidadResponseDTO obtenerEspecialidadPorId(Long id);
    List<EspecialidadResponseDTO> obtenerEspecialidades();
}
