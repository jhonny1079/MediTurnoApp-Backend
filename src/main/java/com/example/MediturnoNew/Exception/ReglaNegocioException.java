package com.example.MediturnoNew.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Se lanza cuando se viola una regla de negocio (ej: turno solapado, estado inválido). */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
