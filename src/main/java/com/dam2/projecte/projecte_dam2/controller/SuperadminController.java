package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.repository.RolRepository;
import com.dam2.projecte.projecte_dam2.service.UsuarioService;

@Controller
public class SuperadminController {

    private final RolRepository rolRepository;
    private final UsuarioService usuarioService;

    public SuperadminController(RolRepository rolRepository, UsuarioService usuarioService) {
        this.rolRepository = rolRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/superadmin/crear_admin")
    public String mostrarCrearAdmin(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "superadmin/crear_admin";
    }

    @PostMapping("/superadmin/crear_admin")
    public String procesarCrearAdmin(@ModelAttribute UsuarioRegistroDTO usuarioRegistroDTO) {
        // Guardado básico; la asignación de roles avanzados puede implementarse aquí
        usuarioService.saveUser(usuarioRegistroDTO);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/superadmin/modificar_usuario")
    public String mostrarModificarUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        model.addAttribute("rolesDisponibles", rolRepository.findAll());
        return "superadmin/modificar_usuario";
    }

    @PostMapping("/superadmin/modificar_usuario")
    public String procesarModificarUsuario(@ModelAttribute UsuarioRegistroDTO usuarioRegistroDTO) {
        // Implementación de modificación según la lógica de negocio (omitida por simplicidad)
        return "redirect:/admin/usuarios";
    }
}
