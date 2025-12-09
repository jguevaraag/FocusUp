package com.dam2.projecte.projecte_dam2.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dam2.projecte.projecte_dam2.model.Rol;
import com.dam2.projecte.projecte_dam2.model.Usuario;
import com.dam2.projecte.projecte_dam2.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findBynombreUsuario(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o password inválidos.");
        }

        return new org.springframework.security.core.userdetails.User(
            usuario.getNombreUsuario(),
            usuario.getPassword(),
            true, // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            !usuario.isBloqueado(), // accountNonLocked -> false cuando esté bloqueado
            mapRolesToAuthorities(usuario.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Rol> roles) {
        return roles.stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toList());
    }
}
