package com.example.MediturnoNew.DTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespuestaJwt {
    private String token;
    private String tipo = "Bearer";
    private String nombreUsuario;
    private String rol;
    private Long usuarioId;
}
