package com.example.MediturnoNew.Controller;

import com.example.MediturnoNew.DTO.Turno.TurnoRequestDTO;
import com.example.MediturnoNew.DTO.Turno.TurnoResponseDTO;
import com.example.MediturnoNew.Enumeraciones.EstadoTurno;
import com.example.MediturnoNew.Service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller de Turnos.
 * Solo delega al Service — ninguna lógica aquí.

 * Endpoints:
 *   POST   /api/turnos                          → crear turno
 *   GET    /api/turnos                          → listar todos
 *   GET    /api/turnos/{id}                     → obtener uno
 *   GET    /api/turnos/paciente/{pacienteId}    → turnos de un paciente
 *   GET    /api/turnos/medico/{medicoId}?fecha= → agenda del médico por fecha
 *   PATCH  /api/turnos/{id}/estado              → cambiar estado
 *   DELETE /api/turnos/{id}                     → cancelar
 */
@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    // POST /api/turnos
    // @Valid activa las validaciones del DTO (NotNull, FutureOrPresent, etc.)
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crear(@Valid @RequestBody TurnoRequestDTO dto) {
        TurnoResponseDTO respuesta = turnoService.crearTurno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // GET /api/turnos
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(turnoService.obtenerTodos());
    }

    // GET /api/turnos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerTurnoPorId(id));
    }

    // GET /api/turnos/paciente/3
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<TurnoResponseDTO>> porPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorPaciente(pacienteId));
    }

    // GET /api/turnos/medico/5?fecha=2025-06-20
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<TurnoResponseDTO>> porMedicoYFecha(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorMedicoYFecha(medicoId, fecha));
    }

    // PATCH /api/turnos/3/estado?nuevoEstado=CONFIRMADO
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TurnoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTurno nuevoEstado) {
        return ResponseEntity.ok(turnoService.cambiarEstado(id, nuevoEstado));
    }

    // DELETE /api/turnos/3   → cancela (no borra de la BD)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.noContent().build();
    }
}
