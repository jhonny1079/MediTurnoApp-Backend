package com.example.MediturnoNew.Service.Impl;



import com.example.MediturnoNew.DTO.Horarío.HorarioRequestDTO;
import com.example.MediturnoNew.DTO.Horarío.HorarioResponseDTO;
import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Service.HorarioService;
import com.example.MediturnoNew.Model.HorarioDisponible;
import com.example.MediturnoNew.Model.Medico;
import com.example.MediturnoNew.Repository.HorarioDisponibleRepository;
import com.example.MediturnoNew.Repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final HorarioDisponibleRepository horarioRepository;
    private final MedicoRepository medicoRepository;

    @Override
    @Transactional
    public HorarioResponseDTO crearHorario(HorarioRequestDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con id: " + dto.getMedicoId()));

        if (dto.getHoraFin().isBefore(dto.getHoraInicio()) ||
                dto.getHoraFin().equals(dto.getHoraInicio())) {
            throw new ReglaNegocioException("La hora de fin debe ser posterior a la hora de inicio");
        }

        HorarioDisponible horario = HorarioDisponible.builder()
                .medico(medico)
                .diaSemana(dto.getDiaSemana())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();

        return mapearAResponseDTO(horarioRepository.save(horario));
    }

    @Override
    @Transactional
    public HorarioResponseDTO actualizarHorario(Long id, HorarioRequestDTO dto) {
        HorarioDisponible horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Horario no encontrado con id: " + id));

        if (dto.getHoraFin().isBefore(dto.getHoraInicio())) {
            throw new ReglaNegocioException("La hora de fin debe ser posterior a la hora de inicio");
        }

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setActivo(dto.getActivo());

        return mapearAResponseDTO(horarioRepository.save(horario));
    }

    @Override
    @Transactional
    public void eliminarHorario(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Horario no encontrado con id: " + id);
        }
        horarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioResponseDTO obtenerHorarioPorId(Long id) {
        return horarioRepository.findById(id)
                .map(this::mapearAResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Horario no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponseDTO> obtenerHorariosPorMedico(Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con id: " + medicoId));

        return horarioRepository.findAll()
                .stream()
                .filter(h -> h.getMedico().getId().equals(medico.getId()))
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponseDTO> obtenerHorariosDisponibles(Long medicoId, LocalDate fecha) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con id: " + medicoId));

        // getDayOfWeek().getValue() devuelve 1=lunes...7=domingo (igual que el modelo)
        int diaSemana = fecha.getDayOfWeek().getValue();

        return horarioRepository.findActivosPorMedicoYFecha(medico, diaSemana, fecha)
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    private HorarioResponseDTO mapearAResponseDTO(HorarioDisponible h) {
        return HorarioResponseDTO.builder()
                .id(h.getId())
                .medicoId(h.getMedico().getId())
                .medicoNombre(h.getMedico().getNombre() + " " + h.getMedico().getApellido())
                .diaSemana(h.getDiaSemana())
                .horaInicio(h.getHoraInicio())
                .horaFin(h.getHoraFin())
                .activo(h.getActivo())
                .build();
    }
}
