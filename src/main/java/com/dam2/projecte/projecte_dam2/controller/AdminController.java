package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/usuarios")
    public String verUsuarios() {
        return "admin/usuarios";
    }
}
