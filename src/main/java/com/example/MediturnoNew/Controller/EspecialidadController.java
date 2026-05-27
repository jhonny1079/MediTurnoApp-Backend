package com.example.MediturnoNew.Controller;



import com.example.MediturnoNew.DTO.Especialidad.EspecialidadRequestDTO;
import com.example.MediturnoNew.DTO.Especialidad.EspecialidadResponseDTO;
import com.example.MediturnoNew.Service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints:
 *   GET    /api/especialidades       → listar todas  (autenticado)
 *   GET    /api/especialidades/{id}  → obtener por id
 *   POST   /api/especialidades       → crear         (ADMIN)
 *   PUT    /api/especialidades/{id}  → actualizar    (ADMIN)
 *   DELETE /api/especialidades/{id}  → eliminar      (ADMIN)
 */
@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listar() {
        return ResponseEntity.ok(especialidadService.obtenerEspecialidades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.obtenerEspecialidadPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<EspecialidadResponseDTO> crear(@Valid @RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadService.crearEspecialidad(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<EspecialidadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.ok(especialidadService.actualizarEspecialidad(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
