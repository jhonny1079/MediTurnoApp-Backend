package com.example.MediturnoNew.Controller;



import com.example.MediturnoNew.DTO.Medico.MedicoRequestDTO;
import com.example.MediturnoNew.DTO.Medico.MedicoResponseDTO;
import com.example.MediturnoNew.Service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints:
 *   GET    /api/medicos                               → listar todos
 *   GET    /api/medicos/{id}                          → obtener por id
 *   GET    /api/medicos/especialidad/{especialidadId} → filtrar por especialidad
 *   POST   /api/medicos                               → crear       (ADMIN)
 *   PUT    /api/medicos/{id}                          → actualizar  (ADMIN)
 *   DELETE /api/medicos/{id}                          → eliminar    (ADMIN)
 */
@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listar() {
        return ResponseEntity.ok(medicoService.obtenerMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.obtenerMedicoPorId(id));
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<MedicoResponseDTO>> porEspecialidad(@PathVariable Long especialidadId) {
        return ResponseEntity.ok(medicoService.obtenerMedicosPorEspecialidad(especialidadId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<MedicoResponseDTO> crear(@Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.crearMedico(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<MedicoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.ok(medicoService.actualizarMedico(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }
}