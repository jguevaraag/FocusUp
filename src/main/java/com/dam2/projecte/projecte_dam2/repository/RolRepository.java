package com.dam2.projecte.projecte_dam2.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dam2.projecte.projecte_dam2.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
    // Método para buscar un rol por su nombre
    Rol findByNombre(String nombre); 
}
