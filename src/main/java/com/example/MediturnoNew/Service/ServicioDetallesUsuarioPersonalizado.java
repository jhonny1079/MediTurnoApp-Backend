package com.example.MediturnoNew.Service;


import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Servicio que carga los datos del usuario desde la base de datos
 * para el proceso de autenticación de Spring Security.
 */
@Service
public class ServicioDetallesUsuarioPersonalizado implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServicioDetallesUsuarioPersonalizado(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)  // Nota: usa el @Transactional de Spring
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        // Buscar al usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));

        // Construir el UserDetails de Spring con los datos del usuario
        // El constructor es: User(username, password, enabled, accountNonExpired,
        //                       credentialsNonExpired, accountNonLocked, authorities)
        return new User(
                usuario.getNombreUsuario(),
                usuario.getContrasena(),
                usuario.getActivo(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().name()))
        );
    }
}
