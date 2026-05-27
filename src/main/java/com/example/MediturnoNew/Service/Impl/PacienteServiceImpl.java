package com.example.MediturnoNew.Service.Impl;


import com.example.MediturnoNew.DTO.Paciente.PacienteRequestDTO;
import com.example.MediturnoNew.DTO.Paciente.PacienteResponseDTO;
import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Service.PacienteService;
import com.example.MediturnoNew.Model.Paciente;
import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.PacienteRepository;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PacienteResponseDTO crearPaciente(PacienteRequestDTO dto) {
        if (pacienteRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new ReglaNegocioException(
                    "Ya existe un paciente con la cedula: " + dto.getCedula());
        }
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + dto.getUsuarioId()));
        Paciente paciente = Paciente.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .cedula(dto.getCedula())
                .fechaNacimiento(dto.getFechaNacimiento())
                .seguroMedico(dto.getSeguroMedico())
                .usuario(usuario)
                .build();
        return mapearAResponseDTO(pacienteRepository.save(paciente));
    }

    @Override
    @Transactional
    public PacienteResponseDTO actualizarPaciente(Long id, PacienteRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con id: " + id));
        if (!paciente.getCedula().equals(dto.getCedula()) &&
                pacienteRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new ReglaNegocioException(
                    "Ya existe un paciente con la cedula: " + dto.getCedula());
        }
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setCedula(dto.getCedula());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSeguroMedico(dto.getSeguroMedico());
        return mapearAResponseDTO(pacienteRepository.save(paciente));
    }

    @Override
    @Transactional
    public void eliminarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Paciente no encontrado con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDTO obtenerPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .map(this::mapearAResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDTO obtenerPacientePorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula)
                .map(this::mapearAResponseDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con cedula: " + cedula));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> obtenerPacientes() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)

    public PacienteResponseDTO obtenerPacientePorNombreUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado: " + nombreUsuario));
        Paciente paciente = pacienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Perfil no encontrado para: " + nombreUsuario));
        return mapearAResponseDTO(paciente);
    }


    private PacienteResponseDTO mapearAResponseDTO(Paciente p) {
        return PacienteResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .apellido(p.getApellido())
                .cedula(p.getCedula())
                .fechaNacimiento(p.getFechaNacimiento())
                .seguroMedico(p.getSeguroMedico())
                .usuarioId(p.getUsuario() != null ? p.getUsuario().getId() : null)
                .email(p.getUsuario() != null ? p.getUsuario().getEmail() : null)
                .build();
    }
}