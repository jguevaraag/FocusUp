package com.dam2.projecte.projecte_dam2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dam2.projecte.projecte_dam2.model.RegistroSesion;

@Repository
public interface RegistroSesionRepository extends JpaRepository<RegistroSesion, Long> {

}
