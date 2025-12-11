package com.dam2.projecte.projecte_dam2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/index_super")
    public String indexSuperadmin() {
        return "index_super";
    }

    @GetMapping("/index_admin")
    public String indexAdmin() {
        return "index_admin";
    }

    @GetMapping("/admin/usuarios")
    public String verUsuarios() {
        return "admin/usuarios";
    }
}
