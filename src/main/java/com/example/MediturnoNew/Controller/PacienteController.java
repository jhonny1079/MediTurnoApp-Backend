package com.example.MediturnoNew.Controller;



import com.example.MediturnoNew.DTO.Paciente.PacienteRequestDTO;
import com.example.MediturnoNew.DTO.Paciente.PacienteResponseDTO;
import com.example.MediturnoNew.Service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping("/mi-perfil")
    @PreAuthorize("hasAnyRole('ROLE_PACIENTE', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<PacienteResponseDTO> miPerfil(Authentication authentication) {
        String nombreUsuario = authentication.getName();
        return ResponseEntity.ok(pacienteService.obtenerPacientePorNombreUsuario(nombreUsuario));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONITA')")
    public ResponseEntity<List<PacienteResponseDTO>> listar() {
        return ResponseEntity.ok(pacienteService.obtenerPacientes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONITA', 'ROLE_PACIENTE')")
    public ResponseEntity<PacienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.obtenerPacientePorId(id));
    }

    @GetMapping("/cedula/{cedula}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONITA')")
    public ResponseEntity<PacienteResponseDTO> obtenerPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(pacienteService.obtenerPacientePorCedula(cedula));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONITA')")
    public ResponseEntity<PacienteResponseDTO> crear(@Valid @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.crearPaciente(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_RECEPCIONITA')")
    public ResponseEntity<PacienteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}


