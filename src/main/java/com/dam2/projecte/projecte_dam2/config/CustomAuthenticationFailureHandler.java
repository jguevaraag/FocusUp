package com.dam2.projecte.projecte_dam2.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.LockedException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.dam2.projecte.projecte_dam2.model.RegistroSesion;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.RegistroSesionRepository;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final int MAX_ATTEMPTS = 3;
    private final UsuarioRepository usuarioRepository;
    private final RegistroSesionRepository registroSesionRepository;

    public CustomAuthenticationFailureHandler(UsuarioRepository usuarioRepository,
            RegistroSesionRepository registroSesionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.registroSesionRepository = registroSesionRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // Si la excepción es LockedException (cuenta bloqueada por Spring), redirigimos
        // con el flag 'bloqueado'
        if (exception instanceof LockedException) {
            super.setDefaultFailureUrl("/login?error&bloqueado");
            super.onAuthenticationFailure(request, response, exception);
            return;
        }

        String username = request.getParameter("username");
        Usuario usuario = usuarioRepository.findBynombreUsuario(username);

        if (usuario != null) {
            if (usuario.isBloqueado()) {
                super.setDefaultFailureUrl("/login?error&bloqueado");
                // Lógica de registro de sesión fallida
                String ipAddress = request.getRemoteAddr();
                RegistroSesion registroSesion = new RegistroSesion(usuario, ipAddress);
                registroSesionRepository.save(registroSesion);
            } else {
                // 1. Incrementar intentos fallidos
                int intentos = usuario.getIntentosFallidos() + 1;
                usuario.setIntentosFallidos(intentos);

                // 2. Comprobar si debe ser bloqueado
                if (intentos >= MAX_ATTEMPTS) {
                    usuario.setBloqueado(true);
                    super.setDefaultFailureUrl("/login?error&bloqueado");
                    usuario.setIntentosFallidos(0);
                } else {
                    super.setDefaultFailureUrl("/login?error");
                }

                // 3. Guardar cambios
                usuarioRepository.save(usuario);

                // Lógica de registro de sesión fallida
                String ipAddress = request.getRemoteAddr();
                RegistroSesion registroSesion = new RegistroSesion(usuario, ipAddress);
                registroSesionRepository.save(registroSesion);
            }
        } else {
            // Usuario no encontrado
            super.setDefaultFailureUrl("/login?error");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
