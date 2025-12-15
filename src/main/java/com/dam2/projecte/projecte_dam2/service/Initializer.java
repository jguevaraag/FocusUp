package com.dam2.projecte.projecte_dam2.service;

import com.dam2.projecte.projecte_dam2.model.Rol;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.RolRepository;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class Initializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "superadmin";
    private static final String ADMIN_EMAIL = "superadmin@example.com";
    private static final String ADMIN_ROLE = "ROLE_SUPERADMIN";
    private static final String PLAIN_PASSWORD = "SuperAdmin123!";

    public Initializer(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Verificar y crear el rol ROLE_SUPERADMIN si no existe
        final String SUPERADMIN_ROLE_NAME = ADMIN_ROLE;
        Rol superAdminRole = rolRepository.findByNombre(SUPERADMIN_ROLE_NAME);
        if (superAdminRole == null) {
            superAdminRole = rolRepository.save(new Rol(SUPERADMIN_ROLE_NAME));
            System.out.println(">> Rol " + SUPERADMIN_ROLE_NAME + " creado exitosamente.");
        }

        if (rolRepository.findByNombre("ROLE_ADMIN") == null) {
            rolRepository.save(new Rol("ROLE_ADMIN"));
            System.out.println(">> Rol ROLE_ADMIN creado exitosamente.");
        }

        if (rolRepository.findByNombre("ROLE_USER") == null) {
            rolRepository.save(new Rol("ROLE_USER"));
            System.out.println(">> Rol ROLE_USER creado exitosamente.");
        }

        // 2. Crear usuario superadmin si no existe
        if (usuarioRepository.findBynombreUsuario(ADMIN_USERNAME) == null) {
            Collection<Rol> roles = new ArrayList<>();
            roles.add(superAdminRole);

            Rol adminRole = rolRepository.findByNombre("ROLE_ADMIN");
            if (adminRole != null)
                roles.add(adminRole);

            Rol userRole = rolRepository.findByNombre("ROLE_USER");
            if (userRole != null)
                roles.add(userRole);

            Usuario superAdmin = new Usuario(
                    "Master",
                    "System",
                    ADMIN_EMAIL,
                    ADMIN_USERNAME,
                    passwordEncoder.encode(PLAIN_PASSWORD),
                    roles);

            usuarioRepository.save(superAdmin);
            System.out.println(">> Usuario SuperAdmin inicializado con éxito.");
        }
    }
}