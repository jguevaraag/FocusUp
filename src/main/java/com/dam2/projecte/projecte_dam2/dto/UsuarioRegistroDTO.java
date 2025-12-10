package com.dam2.projecte.projecte_dam2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioRegistroDTO {

    private long id;

    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

   
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

   
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Introduce un correo electrónico válido")
    private String email;

    
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String nombreUsuario; 

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\$;._\\-\\*]).{8,}$",
        message = "La contraseña debe incluir al menos una mayúscula, una minúscula, un número y un símbolo ($;._-*)"
    )
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
