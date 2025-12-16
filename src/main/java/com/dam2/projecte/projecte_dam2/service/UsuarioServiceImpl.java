package com.dam2.projecte.projecte_dam2.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
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
    public Usuario updateUser(UsuarioRegistroDTO updateDTO) {
        // 1. Buscamos el usuario existente por su ID (que viene en el DTO/Contexto de
        // seguridad)
        Optional<Usuario> optionalUser = usuarioRepository.findById(updateDTO.getId());

        if (optionalUser.isEmpty()) {
            // (Manejo de error)
        }

        Usuario existingUser = optionalUser.get();

        // 2. Actualizar SOLAMENTE los campos permitidos
        existingUser.setNombre(updateDTO.getNombre());
        existingUser.setApellidos(updateDTO.getApellidos());
        existingUser.setEmail(updateDTO.getEmail());

        // 3. Lógica para la contraseña: Solo se codifica y se actualiza si se recibe
        // una nueva
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        // 4. Guardar los cambios. El objeto existingUser ya contiene el ID
        // y la contraseña ENCRIPTADA original, si no fue modificada en el paso 3.
        return usuarioRepository.save(existingUser);
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findBynombreUsuario(nombreUsuario);
    }

   
}