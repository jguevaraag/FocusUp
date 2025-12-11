package com.dam2.projecte.projecte_dam2.config;
import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
            // Lógica de seguridad: resetear intentos fallidos y verificar bloqueo
            if (usuario.getIntentosFallidos() > 0) {
                usuario.setIntentosFallidos(0);
                usuarioRepository.save(usuario);
            } else if (usuario.isBloqueado()) {
                throw new ServletException("Usuario bloqueado");
            }
            
            // Lógica de registro de sesión
            String ipAddress = request.getRemoteAddr();
            // Asumiendo que la clase RegistroSesion existe
             RegistroSesion registroSesion = new RegistroSesion(usuario, ipAddress);
             registroSesionRepository.save(registroSesion);
        }

        // ----------------------------------------------------
        // INICIO: LÓGICA DE REDIRECCIÓN CONDICIONAL BASADA EN ROLES (Paso 4)
        // ----------------------------------------------------
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String targetUrl = determineTargetUrl(authorities);

        // Se ejecuta la redirección explícita y se evita la llamada a super.onAuthenticationSuccess
        if (response.isCommitted()) {
            // Si la respuesta ya ha sido enviada (committed), no se puede redirigir
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    
    /**
     * Determina la URL de redirección en base a los roles del usuario.
     */
    protected String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        String targetUrl = "/index_user"; // Default para ROLE_USER o si no se encuentra un rol específico

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_SUPERADMIN")) {
                // Prioridad máxima: si es SuperAdmin, redirige y termina la búsqueda.
                return "/index_super";
            } else if (role.equals("ROLE_ADMIN")) {
                // Si es Admin (pero no SuperAdmin, ya que el anterior ya habría retornado)
                targetUrl = "/index_admin";
            }
        }
        return targetUrl;
    }
}
