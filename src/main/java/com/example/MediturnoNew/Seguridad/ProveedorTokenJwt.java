package com.example.MediturnoNew.Seguridad;



import com.example.MediturnoNew.Model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProveedorTokenJwt {

    @Value("${app.jwt.secreto}")
    private String secreto;

    @Value("${app.jwt.expiracion}")
    private long expiracion;

    private SecretKey clave;

    @PostConstruct
    public void iniciar() {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes());
    }

    /**
     * Genera un token JWT para el usuario autenticado.
     */
    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", usuario.getRol().name());
        claims.put("usuarioId", usuario.getId());

        Date ahora = new Date();
        Date vencimiento = new Date(ahora.getTime() + expiracion);

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getNombreUsuario())
                .issuedAt(ahora)
                .expiration(vencimiento)
                .signWith(clave)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) del token.
     */
    public String obtenerNombreUsuarioDelToken(String token) {
        return obtenerClaims(token).getSubject();
    }

    /**
     * Valida el token comprobando su firma y fecha de expiración.
     */
    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parsea y valida el token para obtener los claims.
     */
    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
