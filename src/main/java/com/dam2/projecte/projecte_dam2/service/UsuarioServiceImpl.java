package com.dam2.projecte.projecte_dam2.service;

import java.util.Arrays;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.model.Rol;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder; // Añadido para codificar la contraseña

    // Constructor actualizado para inyectar PasswordEncoder
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super();
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario saveUser(UsuarioRegistroDTO registroDTO) {
        // Se añade nombreUsuario y se codifica la contraseña
        Usuario usuario = new Usuario(
            registroDTO.getNombre(), 
            registroDTO.getApellidos(), 
            registroDTO.getEmail(),
            registroDTO.getNombreUsuario(), // Nuevo campo
            passwordEncoder.encode(registroDTO.getPassword()), // Contraseña codificada
            Arrays.asList(new Rol("ROLE_USER"))
        );
        return usuarioRepository.save(usuario);
    }


}
