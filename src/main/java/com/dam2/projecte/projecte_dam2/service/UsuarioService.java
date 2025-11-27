package com.dam2.projecte.projecte_dam2.service;

import com.dam2.projecte.projecte_dam2.dto.UsuarioRegistroDTO;
import com.dam2.projecte.projecte_dam2.model.Usuario;

public interface UsuarioService {

    public Usuario saveUser(UsuarioRegistroDTO registroDTO);
}
