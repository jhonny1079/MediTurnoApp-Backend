package com.example.MediturnoNew.Service;

import com.example.MediturnoNew.Model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> obtenerUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    Usuario crearUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void eliminarUsuario(Long id);
    boolean existeByNombreUsuario(String nombreUsuario);
}
