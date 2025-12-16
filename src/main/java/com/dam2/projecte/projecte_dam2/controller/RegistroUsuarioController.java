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
}
