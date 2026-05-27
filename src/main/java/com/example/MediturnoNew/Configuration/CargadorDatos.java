package com.example.MediturnoNew.Configuration;


import com.example.MediturnoNew.Enumeraciones.Rol;
import com.example.MediturnoNew.Model.Especialidad;
import com.example.MediturnoNew.Model.Usuario;
import com.example.MediturnoNew.Repository.EspecialidadRepository;
import com.example.MediturnoNew.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CargadorDatos implements CommandLineRunner {
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder codificadorContrasena;

    public CargadorDatos(EspecialidadRepository especialidadRepository, UsuarioRepository usuarioRepositorio, PasswordEncoder codificadorContrasena) {
        this.especialidadRepository = especialidadRepository;
        this.usuarioRepository = usuarioRepositorio;
        this.codificadorContrasena = codificadorContrasena;
    }
    @Override
    public void run(String... argumentos){
        if(especialidadRepository.count()==0){
            especialidadRepository.save(new Especialidad(null,"Cardiología", "Especialidad dedicada al corazón y sistema circulatorio"));
            especialidadRepository.save(new Especialidad(null, "Pediatría", "Atención médica de niños y adolescentes"));
            especialidadRepository.save(new Especialidad(null, "Dermatología", "Tratamiento de enfermedades de la piel"));
            especialidadRepository.save(new Especialidad(null, "Clínica General", "Medicina general y atención primaria"));
        }

        // Crear un usuario administrador por defecto (admin / admin123)
        if (!usuarioRepository.existsByNombreUsuario("admin")) {
            Usuario admin = Usuario.builder()
                    .nombreUsuario("admin")
                    .email("admin@turnosmedicos.com")
                    .contrasena(codificadorContrasena.encode("admin123"))
                    .rol(Rol.ROLE_ADMINISTRADOR)
                    .activo(true)
                    .build();
            usuarioRepository.save(admin);
        }
    }
}

