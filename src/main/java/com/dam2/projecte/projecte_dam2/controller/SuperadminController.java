package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.repository.RolRepository;
import com.dam2.projecte.projecte_dam2.service.UsuarioService;
import jakarta.validation.Valid; // Asegúrese de importar esto
import org.springframework.validation.BindingResult; // Asegúrese de importar esto

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

    @GetMapping("/superadmin/modificar_usuario")
    public String mostrarModificarUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        model.addAttribute("rolesDisponibles", rolRepository.findAll());
        return "superadmin/modificar_usuario";
    }

    @PostMapping("/superadmin/modificar_usuario")
    public String procesarModificarUsuario(@ModelAttribute UsuarioRegistroDTO usuarioRegistroDTO) {
        // Implementación de modificación según la lógica de negocio (omitida por
        // simplicidad)
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/superadmin/crear_admin")
    public String procesarCrearAdmin(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
            BindingResult result,
            Model model) { // Asegúrate de incluir 'Model' aquí

        // 1. Manejo de errores de validación (si falla, regresa a la vista)
        if (result.hasErrors()) {
            // Asegúrate de que los errores se muestren en la vista
            return "superadmin/crear_admin";
        }

        // 2. Lógica de negocio (creación del administrador)
        // Asumiendo que usted ya implementó el método 'createAdmin' que asigna
        // ROLE_ADMIN
        usuarioService.createAdmin(registroDTO);

        // 3. Preparar el éxito:

        // a) Añadir el flag de éxito al modelo
        model.addAttribute("admin_creado_exito", true);

        // b) Crear un DTO nuevo para "resetear" el formulario
        model.addAttribute("usuario", new UsuarioRegistroDTO());

        // 4. Retornar la misma vista
        return "superadmin/crear_admin";
    }
}
