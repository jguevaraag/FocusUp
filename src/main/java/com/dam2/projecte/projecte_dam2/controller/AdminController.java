package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.dto.UsuarioUpdateDTO;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;
import com.dam2.projecte.projecte_dam2.service.UsuarioService;

import jakarta.validation.Valid;

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

    @GetMapping("/modificar")
    public String mostrarFormularioModificar(Principal principal, Model model) {
        String nombreUsuario = principal.getName();
        Usuario usuario = usuarioService.findByNombreUsuario(nombreUsuario);
        
        if (usuario == null) {
            // Manejar caso donde el usuario autenticado no se encuentra (debería ser imposible)
            return "redirect:/logout"; 
        }

        // Mapear la entidad Usuario a DTO para precargar el formulario
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getNombreUsuario(),
            "" // La contraseña se inicializa vacía
        );
        dto.setId(usuario.getId()); 
        
        model.addAttribute("usuario", dto);
        return "/modificar"; 
    }

    @PostMapping("/modificar")
    public String procesarModificar(@Valid @ModelAttribute("usuario") UsuarioUpdateDTO updateDTO, // Cambio de DTO
                                     BindingResult result, Principal principal) {
        
        // 1. Seguridad: Asegurar que el DTO lleva el ID del usuario autenticado
        String nombreUsuarioActual = principal.getName();
        Usuario usuarioActual = usuarioService.findByNombreUsuario(nombreUsuarioActual);
        
        if (usuarioActual == null) {
            return "redirect:/logout"; 
        }

        // 2. Si el formulario tiene errores de validación (por ejemplo, campos en blanco)
        if (result.hasErrors()) {
            // Asegurarse de que el ID del usuario actual está en el DTO para el servicio
            updateDTO.setId(usuarioActual.getId());
            return "/modificar";
        }
        
        updateDTO.setId(usuarioActual.getId());
        
        // 4. Ejecutar la lógica de negocio (debe existir un método para recibir UsuarioUpdateDTO)
        usuarioService.updateUser(updateDTO);

        // 5. Redireccionar al índice del usuario con un mensaje de éxito
        return "redirect:/index_user?edicion_exitosa";
    }

    @GetMapping("/admin/usuarios")
    public String verUsuarios() {
        return "admin/usuarios";
    }
}
