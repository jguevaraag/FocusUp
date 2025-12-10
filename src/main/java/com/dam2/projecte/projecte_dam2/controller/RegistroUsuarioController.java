package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult; // Importante
import jakarta.validation.Valid;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
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
        usuarioService.saveUser(rUsuarioRegistroDTO);
        return "redirect:/registro?exito";
    }
}
