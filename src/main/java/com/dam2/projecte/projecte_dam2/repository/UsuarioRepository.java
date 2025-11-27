package com.dam2.projecte.projecte_dam2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<com.dam2.projecte.projecte_dam2.model.Usuario, Long> {
    
    com.dam2.projecte.projecte_dam2.model.Usuario findBynombreUsuario(String nombreUsuario);

}
