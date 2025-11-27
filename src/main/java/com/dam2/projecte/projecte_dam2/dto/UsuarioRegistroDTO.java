package com.dam2.projecte.projecte_dam2.dto;


public class UsuarioRegistroDTO {

    private long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String nombreUsuario; // Añadido para coincidir con registro.html
    private String password;

    public UsuarioRegistroDTO() {
        super();
    }

    public UsuarioRegistroDTO(long id, String nombre, String apellidos, String email, String nombreUsuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }


    public UsuarioRegistroDTO(String nombre, String apellidos, String email, String nombreUsuario, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }

    public UsuarioRegistroDTO(String email) {
        this.email = email;
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

    
}
