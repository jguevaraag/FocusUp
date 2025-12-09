package com.dam2.projecte.projecte_dam2.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dam2.projecte.projecte_dam2.model.RegistroSesion;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.RegistroSesionRepository;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final RegistroSesionRepository registroSesionRepository;

    public CustomAuthenticationSuccessHandler(UsuarioRepository usuarioRepository, RegistroSesionRepository registroSesionRepository ) {
        this.usuarioRepository = usuarioRepository;
        this.registroSesionRepository = registroSesionRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String nombreUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findBynombreUsuario(nombreUsuario);

        if (usuario != null) {
            if (usuario.getIntentosFallidos() > 0) {
                usuario.setIntentosFallidos(0);
                usuarioRepository.save(usuario);
            }else if(usuario.isBloqueado()){
                throw new ServletException("Usuario bloqueado");
                
            }

            String ipAddress = request.getRemoteAddr();
            RegistroSesion registroSesion = new RegistroSesion(usuario, ipAddress);
            registroSesionRepository.save(registroSesion);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
