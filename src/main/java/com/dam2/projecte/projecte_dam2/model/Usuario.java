package com.dam2.projecte.projecte_dam2.model;

import java.util.Collection;

import jakarta.persistence.CascadeType;
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

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "nombre_usuario"})) // Añadido uniqueConstraint para nombre_usuario
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

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @jakarta.persistence.JoinColumn(name = "usuario_id", referencedColumnName = "id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "rol_id", referencedColumnName = "id")
    )
    private Collection<Rol> roles;


    public Usuario(long id, String nombre, String apellidos, String email, String nombreUsuario, String password, Collection<Rol> roles) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.roles = roles;
    }


    public Usuario(String nombre, String apellidos, String email, String nombreUsuario, String password, Collection<Rol> roles) {
        super();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.roles = roles;
    }


    public Usuario() {
        super();
    }


    public long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }


    public String getApellidos() {
        return apellidos;
    }


    public String getEmail() {
        return email;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }


    public String getPassword() {
        return password;
    }


    public Collection<Rol> getRoles() {
        return roles;
    }


    public void setId(long id) {
        this.id = id;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setRoles(Collection<Rol> roles) {
        this.roles = roles;
    }

}
