package com.example.MediturnoNew.Seguridad;


import com.example.MediturnoNew.Service.ServicioDetallesUsuarioPersonalizado;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final ProveedorTokenJwt proveedorTokenJwt;
    private final ServicioDetallesUsuarioPersonalizado servicioDetallesUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest peticion,
                                    HttpServletResponse respuesta,
                                    FilterChain cadenaFiltros)
            throws ServletException, IOException{
        String token = obtenerJwtDeLaPeticion(peticion);

        if(StringUtils.hasText(token) && proveedorTokenJwt.validarToken(token)){
            String nombreUsuario = proveedorTokenJwt.obtenerNombreUsuarioDelToken(token);
            UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            detallesUsuario, null, detallesUsuario.getAuthorities());
            autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(peticion));

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }
        cadenaFiltros.doFilter(peticion, respuesta);
    }
    private String obtenerJwtDeLaPeticion(HttpServletRequest peticion) {
        String bearerToken = peticion.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public FiltroAutenticacionJwt(ProveedorTokenJwt proveedorTokenJwt, ServicioDetallesUsuarioPersonalizado servicioDetallesUsuario) {
        this.proveedorTokenJwt = proveedorTokenJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

}
