package com.dam2.projecte.projecte_dam2.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult; // Importante
import jakarta.validation.Valid;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.service.UsuarioService;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioController {

    private UsuarioService usuarioService;

    public RegistroUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDto() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping
    public String registrarCuentaUsuario(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDTO rUsuarioRegistroDTO, // 1. @Valid activa la validación
            BindingResult result) { // 2. Captura los errores

        // 3. Si hay errores, volvemos a mostrar el formulario ("registro") en lugar de redirigir
        // Thymeleaf usará el objeto 'result' para mostrar los mensajes de error en el HTML
        if (result.hasErrors()) {
            return "registro";
        }

        try {
            usuarioService.saveUser(rUsuarioRegistroDTO);
        } catch (RuntimeException e) {
            // Capturamos la excepción lanzada desde el servicio (un error en tiempo de ejecución)
            
            if ("nombreUsuarioDuplicado".equals(e.getMessage())) {
                // Adjuntamos el error al campo 'nombreUsuario'
                result.rejectValue("nombreUsuario", "error.usuario", "El nombre de usuario ya está registrado.");
            } else if ("emailDuplicado".equals(e.getMessage())) {
                // Adjuntamos el error al campo 'email'
                result.rejectValue("email", "error.email", "El correo electrónico ya está registrado.");
            } else {
                // Error inesperado
                result.reject("global.error", "Ha ocurrido un error inesperado durante el registro.");
            }
            
            // Si capturamos cualquier error, volvemos a mostrar el formulario de registro
            if (result.hasErrors()) {
                return "registro";
            }
        }
        return "redirect:/registro?exito";
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
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getNombreUsuario(),
            "" // La contraseña nunca se devuelve precargada por seguridad
        );
        dto.setId(usuario.getId()); // Es crucial pasar el ID para que JPA sepa qué actualizar
        
        model.addAttribute("usuario", dto);
        return "/modificar"; 
    }

    @PostMapping("/modificar")
    public String procesarModificar(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO updateDTO, 
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
        
        // 3. Pasar el ID del usuario autenticado al DTO antes de actualizar
        updateDTO.setId(usuarioActual.getId());
        
        // 4. Ejecutar la lógica de negocio
        usuarioService.updateUser(updateDTO);

        // 5. Redireccionar al índice del usuario con un mensaje de éxito
        return "redirect:/index_user?edicion_exitosa";
    }
}
