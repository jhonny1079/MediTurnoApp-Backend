package com.example.MediturnoNew.Service.Impl;


import com.example.MediturnoNew.DTO.Especialidad.EspecialidadRequestDTO;
import com.example.MediturnoNew.DTO.Especialidad.EspecialidadResponseDTO;
import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Service.EspecialidadService;
import com.example.MediturnoNew.Model.Especialidad;
import com.example.MediturnoNew.Repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    @Transactional
    public EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO dto) {
        if (especialidadRepository.existsByNombre(dto.getNombre())) {
            throw new ReglaNegocioException(
                    "Ya existe una especialidad con el nombre: " + dto.getNombre());
        }

        Especialidad especialidad = Especialidad.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();

        return mapearAResponseDTO(especialidadRepository.save(especialidad));
    }

    @Override
    @Transactional
    public EspecialidadResponseDTO actualizarEspecialidad(Long id, EspecialidadRequestDTO dto) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Especialidad no encontrada con id: " + id));

        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());

        return mapearAResponseDTO(especialidadRepository.save(especialidad));
    }

    @Override
    @Transactional
    public void eliminarEspecialidad(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Especialidad no encontrada con id: " + id);
        }
        especialidadRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponseDTO obtenerEspecialidadPorId(Long id) {
        return especialidadRepository.findById(id)
                .map(this::mapearAResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Especialidad no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponseDTO> obtenerEspecialidades() {
        return especialidadRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    private EspecialidadResponseDTO mapearAResponseDTO(Especialidad e) {
        return EspecialidadResponseDTO.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .build();
    }
}
