package com.dam2.projecte.projecte_dam2.service;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.dto.UsuarioUpdateDTO;
import com.dam2.projecte.projecte_dam2.model.Usuario;

public interface UsuarioService {

    public Usuario saveUser(UsuarioRegistroDTO registroDTO);
    void eliminarUsuario(String nombreUsuario);
    // Para cargar la información del perfil
    Usuario findByNombreUsuario(String nombreUsuario);

    // Para actualizar el perfil
     Usuario updateUser(UsuarioUpdateDTO updateDTO);

     public Usuario createAdmin(UsuarioRegistroDTO registroDTO);
}
