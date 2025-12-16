package com.dam2.projecte.projecte_dam2.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.dto.UsuarioUpdateDTO;
import com.dam2.projecte.projecte_dam2.model.Rol;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

import com.dam2.projecte.projecte_dam2.repository.RolRepository;
import com.dam2.projecte.projecte_dam2.repository.RegistroSesionRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder; // Añadido para codificar la contraseña
    private final RolRepository rolRepository;
    private final RegistroSesionRepository registroSesionRepository;

    // Constructor actualizado para inyectar PasswordEncoder
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            RolRepository rolRepository, RegistroSesionRepository registroSesionRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.registroSesionRepository = registroSesionRepository;
    }

    @Override
    public Usuario saveUser(UsuarioRegistroDTO registroDTO) {

        if (usuarioRepository.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
            throw new RuntimeException("nombreUsuarioDuplicado");
        }
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new RuntimeException("emailDuplicado");
        }
        // 1. Buscar el rol existente en la base de datos
        Rol userRole = rolRepository.findByNombre("ROLE_USER");

        // NOTA: Es crucial que el Initializer se haya ejecutado y haya creado
        // el rol "ROLE_USER" para evitar un NullPointerException aquí.

        // 2. Crear el nuevo usuario y asignar la instancia del rol persistido
        Usuario usuario = new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellidos(),
                registroDTO.getEmail(),
                registroDTO.getNombreUsuario(),
                passwordEncoder.encode(registroDTO.getPassword()),
                Arrays.asList(userRole) // USA la instancia existente (userRole)
        );

        return usuarioRepository.save(usuario);

    }

    @Transactional
    @Override
    public void eliminarUsuario(String nombreUsuario) {
        // 1. Buscar la entidad Usuario
        Usuario usuario = usuarioRepository.findBynombreUsuario(nombreUsuario);

        // 2. Si existe, eliminarla. (JpaRepository provee deleteById o delete)
        if (usuario != null) {
            registroSesionRepository.deleteByUsuarioId(usuario.getId());
            usuarioRepository.delete(usuario);
        }
        // Opcional: Manejar excepción si no se encuentra, aunque en el contexto de un
        // usuario autenticado, siempre debería existir.
    }

    @Transactional
    @Override
    public Usuario updateUser(UsuarioUpdateDTO updateDTO) { // Cambio de DTO
        Optional<Usuario> optionalUser = usuarioRepository.findById(updateDTO.getId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario con ID " + updateDTO.getId() + " no encontrado.");
        }

        Usuario existingUser = optionalUser.get();

        // Actualizar solo los campos permitidos:
        existingUser.setNombre(updateDTO.getNombre());
        existingUser.setApellidos(updateDTO.getApellidos());
        existingUser.setEmail(updateDTO.getEmail());

        // La lógica clave: si el campo está vacío, no se llama al encoder y se conserva
        // la contraseña existente.
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        return usuarioRepository.save(existingUser);
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findBynombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional // Asegura que la operación sea atómica
    public Usuario createAdmin(UsuarioRegistroDTO registroDTO) {
        // 1. Buscar el rol de administrador
        Rol adminRole = rolRepository.findByNombre("ROLE_ADMIN");

        // Opcional: Si el rol no existe, buscar el rol user como fallback o lanzar
        // excepción
        if (adminRole == null) {
            // Manejar si ROLE_ADMIN no se encuentra (puede ser creado en Initializer)
            throw new RuntimeException("El rol ROLE_ADMIN no existe en la base de datos.");
        }

        // 2. Codificar la contraseña y crear la entidad con el rol ADMIN
        Usuario admin = new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellidos(),
                registroDTO.getEmail(),
                registroDTO.getNombreUsuario(),
                passwordEncoder.encode(registroDTO.getPassword()),
                Arrays.asList(adminRole) // Asignar el rol de administrador
        );

        // 3. Guardar en la base de datos
        return usuarioRepository.save(admin);
    }

}