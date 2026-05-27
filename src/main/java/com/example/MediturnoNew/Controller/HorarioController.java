package com.example.MediturnoNew.Controller;



import com.example.MediturnoNew.DTO.Horarío.HorarioRequestDTO;
import com.example.MediturnoNew.DTO.Horarío.HorarioResponseDTO;
import com.example.MediturnoNew.Service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Endpoints:
 *   GET    /api/horarios/{id}                              → obtener uno
 *   GET    /api/horarios/medico/{medicoId}                 → horarios de un médico
 *   GET    /api/horarios/medico/{medicoId}/disponibles?fecha= → horarios libres en una fecha
 *   POST   /api/horarios                                   → crear    (ADMIN)
 *   PUT    /api/horarios/{id}                              → actualizar (ADMIN)
 *   DELETE /api/horarios/{id}                              → eliminar (ADMIN)
 */
@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerHorarioPorId(id));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<HorarioResponseDTO>> porMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(horarioService.obtenerHorariosPorMedico(medicoId));
    }

    // GET /api/horarios/medico/3/disponibles?fecha=2025-06-20
    @GetMapping("/medico/{medicoId}/disponibles")
    public ResponseEntity<List<HorarioResponseDTO>> disponibles(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(horarioService.obtenerHorariosDisponibles(medicoId, fecha));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<HorarioResponseDTO> crear(@Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.crearHorario(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<HorarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.ok(horarioService.actualizarHorario(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
