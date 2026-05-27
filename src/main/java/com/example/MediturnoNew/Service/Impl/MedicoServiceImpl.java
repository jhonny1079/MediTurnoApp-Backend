package com.example.MediturnoNew.Service.Impl;



import com.example.MediturnoNew.DTO.Medico.MedicoRequestDTO;
import com.example.MediturnoNew.DTO.Medico.MedicoResponseDTO;
import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Service.MedicoService;
import com.example.MediturnoNew.Model.Especialidad;
import com.example.MediturnoNew.Model.Medico;
import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.EspecialidadRepository;
import com.example.MediturnoNew.Repository.MedicoRepository;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadRepository especialidadRepository;

    @Override
    @Transactional
    public MedicoResponseDTO crearMedico(MedicoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + dto.getUsuarioId()));

        // Resolver los IDs de especialidades a entidades reales
        Set<Especialidad> especialidades = resolverEspecialidades(dto.getEspecialidadIds());

        Medico medico = Medico.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .matricula(dto.getMatricula())
                .duracionConsultaMinutos(dto.getDuracionConsultaMinutos())
                .usuario(usuario)
                .especialidades(especialidades)
                .build();

        return mapearAResponseDTO(medicoRepository.save(medico));
    }

    @Override
    @Transactional
    public MedicoResponseDTO actualizarMedico(Long id, MedicoRequestDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado con id: " + id));

        Set<Especialidad> especialidades = resolverEspecialidades(dto.getEspecialidadIds());

        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setMatricula(dto.getMatricula());
        medico.setDuracionConsultaMinutos(dto.getDuracionConsultaMinutos());
        medico.setEspecialidades(especialidades);

        return mapearAResponseDTO(medicoRepository.save(medico));
    }

    @Override
    @Transactional
    public void eliminarMedico(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Médico no encontrado con id: " + id);
        }
        medicoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponseDTO obtenerMedicoPorId(Long id) {
        return medicoRepository.findById(id)
                .map(this::mapearAResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponseDTO> obtenerMedicos() {
        return medicoRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponseDTO> obtenerMedicosPorEspecialidad(Long especialidadId) {
        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Especialidad no encontrada con id: " + especialidadId));

        return medicoRepository.findByEspecialidad(especialidad)
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    // Convierte un Set de IDs a un Set de entidades Especialidad validadas
    private Set<Especialidad> resolverEspecialidades(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<Especialidad> resultado = new HashSet<>();
        for (Long especialidadId : ids) {
            Especialidad e = especialidadRepository.findById(especialidadId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Especialidad no encontrada con id: " + especialidadId));
            resultado.add(e);
        }
        return resultado;
    }

    private MedicoResponseDTO mapearAResponseDTO(Medico m) {
        Set<String> nombresEspecialidades = m.getEspecialidades()
                .stream()
                .map(Especialidad::getNombre)
                .collect(Collectors.toSet());

        return MedicoResponseDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .apellido(m.getApellido())
                .matricula(m.getMatricula())
                .duracionConsultaMinutos(m.getDuracionConsultaMinutos())
                .usuarioId(m.getUsuario() != null ? m.getUsuario().getId() : null)
                .email(m.getUsuario() != null ? m.getUsuario().getEmail() : null)
                .especialidades(nombresEspecialidades)
                .build();
    }
}
