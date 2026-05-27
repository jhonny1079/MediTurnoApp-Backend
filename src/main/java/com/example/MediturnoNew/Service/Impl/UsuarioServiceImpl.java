package com.example.MediturnoNew.Service.Impl;



import com.example.MediturnoNew.Exception.RecursoNoEncontradoException;
import com.example.MediturnoNew.Exception.ReglaNegocioException;
import com.example.MediturnoNew.Service.UsuarioService;
import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + id));

        usuario.setNombreUsuario(datosNuevos.getNombreUsuario());
        usuario.setEmail(datosNuevos.getEmail());
        usuario.setRol(datosNuevos.getRol());
        usuario.setActivo(datosNuevos.getActivo());

        if (datosNuevos.getContrasena() != null && !datosNuevos.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(datosNuevos.getContrasena()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        // Validar que no exista el nombre de usuario
        if (existeByNombreUsuario(usuario.getNombreUsuario())) {
            throw new ReglaNegocioException(
                    "El nombre de usuario ya está en uso: " + usuario.getNombreUsuario());
        }
        // Validar que no exista el email
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ReglaNegocioException(
                    "El email ya está registrado: " + usuario.getEmail());
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
}