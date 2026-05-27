package com.example.MediturnoNew.Service;


import com.example.MediturnoNew.DTO.RespuestaJwt;
import com.example.MediturnoNew.DTO.SolicitudLogin;
import com.example.MediturnoNew.DTO.SolicitudRegistro;

public interface AutenticacionService {
    RespuestaJwt autenticar(SolicitudLogin solicitud);
    void registrarUsuario(SolicitudRegistro solicitud);
}
