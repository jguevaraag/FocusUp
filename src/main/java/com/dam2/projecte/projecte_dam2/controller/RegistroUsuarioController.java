package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String registrarCuentaUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO rUsuarioRegistroDTO) {
        usuarioService.saveUser(rUsuarioRegistroDTO);
        return"redirect:/registro?exito";
    }
}
