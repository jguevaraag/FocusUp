package com.dam2.projecte.projecte_dam2.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = { "email", "nombre_usuario" }))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "email")
    private String email;

    @Column(name = "nombre_usuario", unique = true)
    private String nombreUsuario; // Nuevo campo

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_roles", joinColumns = @jakarta.persistence.JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private Collection<Rol> roles;

    @Column(name = "intentos_fallidos")
    private int intentosFallidos;

    @Column(name = "bloqueado")
    private boolean bloqueado;

    public Usuario(long id, String nombre, String apellidos, String email, String nombreUsuario, String password,
            Collection<Rol> roles) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.roles = roles;
        this.intentosFallidos = 0;
        this.bloqueado = false;
    }

    public Usuario(String nombre, String apellidos, String email, String nombreUsuario, String password,
            Collection<Rol> roles) {
        super();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.roles = roles;
        this.intentosFallidos = 0;
        this.bloqueado = false;
    }
}