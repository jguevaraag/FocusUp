package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;
import com.dam2.projecte.projecte_dam2.service.UsuarioService;

import java.security.Principal;


@Controller
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Asume que UsuarioRepository se inyecta
    public void UserController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/index_super")
    public String indexSuperadmin() {
        return "index_super";
    }

    @GetMapping("/index_admin")
    public String indexAdmin() {
        return "index_admin";
    }

    @GetMapping("/index_user")
    public String indexUser(Authentication authentication, Model model) {
        // 1. Obtener el nombre de usuario del principal de seguridad
        String nombreUsuario = authentication.getName(); 
        
        // 2. Buscar la entidad completa Usuario en la DB
        Usuario usuario = usuarioRepository.findBynombreUsuario(nombreUsuario); // Usando tu método de repositorio [4]

        // 3. Añadir el objeto Usuario al modelo
        if (usuario != null) {
            model.addAttribute("usuarioActual", usuario);
        }
        return "index_user"; // Mapea a resources/templates/index_user.html [3]
    }

    @PostMapping("/perfil/eliminar")
    public String procesarEliminacion(Principal principal) {
        // 1. Obtener el nombre del usuario autenticado
        String nombreUsuario = principal.getName(); 
        
        // 2. Ejecutar la lógica de eliminación en la capa de servicio
        usuarioService.eliminarUsuario(nombreUsuario); 
        
        // 3. Forzar el cierre de sesión y la invalidación de la sesión actual.
        // Esto es VITAL porque el usuario ya no existe en la base de datos.
        SecurityContextHolder.clearContext(); // Limpia el contexto de seguridad

        // Redirigir a la URL de logout de Spring Security, la cual invalida la sesión
        // y lo lleva a /login?logout [3].
        return "redirect:/login?logout"; 
    }

    @GetMapping("/admin/usuarios")
    public String verUsuarios() {
        return "admin/usuarios";
    }
}
