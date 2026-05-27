package com.example.MediturnoNew.Service.Impl;



import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Service.TurnoService;
import com.example.MediturnoNew.DTO.Turno.TurnoRequestDTO;
import com.example.MediturnoNew.DTO.Turno.TurnoResponseDTO;
import com.example.MediturnoNew.Enumeraciones.EstadoTurno;
import com.example.MediturnoNew.Model.*;
import com.example.MediturnoNew.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Turnos.
 *
 * FLUJO DE crearTurno():
 *  1. Recibe un TurnoRequestDTO (solo IDs y fecha)
 *  2. Busca cada entidad en su Repository (Paciente, Medico, etc.)
 *  3. Valida que el horario no esté ocupado (usando contarSolapados)
 *  4. Construye la entidad Turno y la guarda
 *  5. Convierte el Turno guardado a TurnoResponseDTO y lo devuelve
 */
@Service
@RequiredArgsConstructor  // Lombok genera el constructor con los repos inyectados
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final HorarioDisponibleRepository horarioRepository;

    // ─────────────────────────────────────────────
    //  CREAR TURNO  (operación principal)
    // ─────────────────────────────────────────────
    @Override
    @Transactional  // si algo falla en el medio, hace rollback automático
    public TurnoResponseDTO crearTurno(TurnoRequestDTO dto) {

        // PASO 1: Buscar cada entidad por su ID
        // Si no existe, lanzamos excepción con mensaje claro
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con id: " + dto.getPacienteId()));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con id: " + dto.getMedicoId()));

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Especialidad no encontrada con id: " + dto.getEspecialidadId()));

        HorarioDisponible horario = horarioRepository.findById(dto.getHorarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Horario no encontrado con id: " + dto.getHorarioId()));

        // PASO 2: Verificar que el médico tenga esa especialidad
        boolean medicoTieneEspecialidad = medico.getEspecialidades().contains(especialidad);
        if (!medicoTieneEspecialidad) {
            throw new ReglaNegocioException(
                    "El médico no atiende la especialidad: " + especialidad.getNombre());
        }

        // PASO 3: Verificar que el horario esté activo
        if (!horario.getActivo()) {
            throw new ReglaNegocioException("El horario seleccionado no está activo");
        }

        // PASO 4: Verificar que no haya otro turno en ese mismo horario
        // Usamos la query personalizada del TurnoRepository
        long turnosSolapados = turnoRepository.contarSolapados(
                medico,
                dto.getFecha(),
                horario.getHoraInicio(),
                horario.getHoraFin()
        );
        if (turnosSolapados > 0) {
            throw new ReglaNegocioException(
                    "El horario ya está ocupado para esa fecha y médico");
        }

        // PASO 5: Construir la entidad Turno con el Builder de Lombok
        Turno nuevoTurno = Turno.builder()
                .paciente(paciente)
                .medico(medico)
                .especialidad(especialidad)
                .horario(horario)
                .fecha(dto.getFecha())
                .horaInicio(horario.getHoraInicio())   // tomamos las horas del horario
                .horaFin(horario.getHoraFin())
                .estado(EstadoTurno.PENDIENTE)          // siempre arranca PENDIENTE
                .motivoConsulta(dto.getMotivoConsulta())
                .build();

        // PASO 6: Guardar en la BD y mapear a ResponseDTO
        Turno guardado = turnoRepository.save(nuevoTurno);
        return mapearAResponseDTO(guardado);
    }

    // ─────────────────────────────────────────────
    //  CONSULTAS
    // ─────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)  // optimización: le dice a JPA que no necesita rastrear cambios
    public TurnoResponseDTO obtenerTurnoPorId(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con id: " + id));
        return mapearAResponseDTO(turno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> obtenerTurnosPorPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado"));

        // Usamos el método del Repository y convertimos cada Turno a DTO con stream
        return turnoRepository
                .findByPacienteOrderByFechaDescHoraInicioDesc(paciente)
                .stream()
                .map(this::mapearAResponseDTO)   // this::mapearAResponseDTO = turno -> mapearAResponseDTO(turno)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> obtenerTurnosPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado"));

        return turnoRepository
                .findByMedicoAndFecha(medico, fecha)
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> obtenerTodos() {
        return turnoRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    //  CAMBIO DE ESTADO
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public TurnoResponseDTO cambiarEstado(Long id, EstadoTurno nuevoEstado) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con id: " + id));

        // No se puede cambiar el estado de un turno ya cancelado
        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new ReglaNegocioException("No se puede modificar un turno cancelado");
        }

        turno.setEstado(nuevoEstado);
        Turno actualizado = turnoRepository.save(turno);
        return mapearAResponseDTO(actualizado);
    }

    // ─────────────────────────────────────────────
    //  CANCELAR
    // ─────────────────────────────────────────────
    @Override
    @Transactional
    public void cancelarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con id: " + id));

        // Solo se pueden cancelar turnos que estén PENDIENTES o CONFIRMADOS
        if (turno.getEstado() != EstadoTurno.PENDIENTE &&
                turno.getEstado() != EstadoTurno.CONFIRMADO) {
            throw new ReglaNegocioException(
                    "Solo se pueden cancelar turnos en estado PENDIENTE o CONFIRMADO");
        }

        turno.setEstado(EstadoTurno.CANCELADO);
        turnoRepository.save(turno);
    }

    // ─────────────────────────────────────────────
    //  MÉTODO PRIVADO: Entidad → ResponseDTO
    //  Se reutiliza en todos los métodos de arriba
    // ─────────────────────────────────────────────
    private TurnoResponseDTO mapearAResponseDTO(Turno turno) {
        return TurnoResponseDTO.builder()
                // Datos del paciente
                .pacienteId(turno.getPaciente().getId())
                .pacienteNombre(turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido())
                .pacienteCedula(turno.getPaciente().getCedula())
                // Datos del médico
                .medicoId(turno.getMedico().getId())
                .medicoNombre(turno.getMedico().getNombre() + " " + turno.getMedico().getApellido())
                .medicoMatricula(turno.getMedico().getMatricula())
                // Datos de la especialidad
                .especialidadId(turno.getEspecialidad().getId())
                .especialidadNombre(turno.getEspecialidad().getNombre())
                // Datos del turno
                .id(turno.getId())
                .fecha(turno.getFecha())
                .horaInicio(turno.getHoraInicio())
                .horaFin(turno.getHoraFin())
                .estado(turno.getEstado())
                .motivoConsulta(turno.getMotivoConsulta())
                .notas(turno.getNotas())
                .build();
    }
}