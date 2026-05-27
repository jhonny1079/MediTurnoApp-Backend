package com.example.MediturnoNew.Service.Impl;

import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Seguridad.ProveedorTokenJwt;
import com.example.MediturnoNew.Service.AutenticacionService;
import com.example.MediturnoNew.DTO.SolicitudRegistro;
import com.example.MediturnoNew.Enumeraciones.Rol;
import com.example.MediturnoNew.Model.Paciente;
import com.example.MediturnoNew.DTO.RespuestaJwt;
import com.example.MediturnoNew.DTO.SolicitudLogin;
import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutenticacionServiceImpl implements AutenticacionService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProveedorTokenJwt proveedorTokenJwt;

    @Override
    public RespuestaJwt autenticar(SolicitudLogin solicitud) {
        Authentication autenticacion = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        solicitud.getNombreUsuario(),
                        solicitud.getContrasena()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(autenticacion);

        Usuario usuario = usuarioRepository.findByNombreUsuario(solicitud.getNombreUsuario())
                .orElseThrow(() -> new ReglaNegocioException("Usuario no encontrado"));

        String token = proveedorTokenJwt.generarToken(usuario);

        return RespuestaJwt.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    @Transactional
    public void registrarUsuario(SolicitudRegistro solicitud) {
        if (usuarioRepository.existsByNombreUsuario(solicitud.getNombreUsuario())) {
            throw new ReglaNegocioException(
                    "El nombre de usuario ya está en uso: " + solicitud.getNombreUsuario());
        }
        if (usuarioRepository.existsByEmail(solicitud.getEmail())) {
            throw new ReglaNegocioException(
                    "El email ya está registrado: " + solicitud.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(solicitud.getNombreUsuario())
                .email(solicitud.getEmail())
                .contrasena(passwordEncoder.encode(solicitud.getContrasena()))
                .rol(Rol.ROLE_PACIENTE)
                .activo(true)
                .build();

        Paciente paciente = Paciente.builder()
                .nombre(solicitud.getNombre())
                .apellido(solicitud.getApellido())
                .cedula(solicitud.getCedula())
                .seguroMedico(solicitud.getSeguroMedico())
                .usuario(usuario)
                .build();

        usuario.setPaciente(paciente);
        usuarioRepository.save(usuario);
    }
}
