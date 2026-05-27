package com.example.MediturnoNew.Controller;


import com.example.MediturnoNew.DTO.RespuestaJwt;
import com.example.MediturnoNew.DTO.SolicitudLogin;
import com.example.MediturnoNew.DTO.SolicitudRegistro;
import com.example.MediturnoNew.Service.AutenticacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<RespuestaJwt> login(@Valid @RequestBody SolicitudLogin solicitud) {
        return ResponseEntity.ok(autenticacionService.autenticar(solicitud));
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@Valid @RequestBody SolicitudRegistro solicitud) {
        autenticacionService.registrarUsuario(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario registrado exitosamente");
    }
}